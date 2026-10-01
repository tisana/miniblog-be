<#
.SYNOPSIS
Generates an HS512 JWT valid for 10 hours for local backend testing.
.EXAMPLE
$token = ./scripts/New-LocalJwt.ps1
Invoke-RestMethod http://localhost:8081/api/cards -Headers @{ Authorization = "Bearer $token" }
#>
[CmdletBinding()]
param(
    [ValidateNotNullOrEmpty()]
    [string]$Subject = 'admin',

    [ValidateNotNullOrEmpty()]
    [string[]]$Authorities = @('ROLE_ADMIN', 'ROLE_USER'),

    [string]$Base64Secret = $env:JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

function ConvertTo-Base64Url {
    param([byte[]]$Bytes)
    [Convert]::ToBase64String($Bytes).TrimEnd('=').Replace('+', '-').Replace('/', '_')
}

if ([string]::IsNullOrWhiteSpace($Base64Secret)) {
    # The dev profile includes secret-samples. Resolve it relative to this script.
    $configPath = Join-Path $PSScriptRoot '../src/main/resources/config/application-secret-samples.yml'
    $config = Get-Content -LiteralPath $configPath -Raw
    $match = [regex]::Match($config, '(?m)^\s*base64-secret:\s*(\S+)\s*$')
    if (-not $match.Success) {
        throw 'No local sample key found. Supply -Base64Secret or set JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET.'
    }
    $Base64Secret = $match.Groups[1].Value
}

try {
    $key = [Convert]::FromBase64String($Base64Secret)
} catch {
    throw 'The JWT key must be valid Base64.'
}
if ($key.Length -lt 64) {
    throw 'HS512 requires a JWT key of at least 64 bytes.'
}

$issuedAt = [DateTimeOffset]::UtcNow.ToUnixTimeSeconds()
$header = @{ alg = 'HS512'; typ = 'JWT' } | ConvertTo-Json -Compress
$payload = [ordered]@{
    sub = $Subject
    auth = @($Authorities)
    iat = $issuedAt
    exp = $issuedAt + 36000
} | ConvertTo-Json -Compress

$encodedHeader = ConvertTo-Base64Url ([Text.Encoding]::UTF8.GetBytes($header))
$encodedPayload = ConvertTo-Base64Url ([Text.Encoding]::UTF8.GetBytes($payload))
$signingInput = "$encodedHeader.$encodedPayload"
$hmac = [Security.Cryptography.HMACSHA512]::new($key)
try {
    $signature = ConvertTo-Base64Url ($hmac.ComputeHash([Text.Encoding]::UTF8.GetBytes($signingInput)))
} finally {
    $hmac.Dispose()
}

# Return only the token so callers can capture it or pipe it to Set-Clipboard.
"$signingInput.$signature"
