$ErrorActionPreference = "Stop"

$root = (Resolve-Path (Join-Path $PSScriptRoot ".."))
$contractPath = Join-Path $root "contracts\pacts\checkout-service-catalog-service.json"
$original = [IO.File]::ReadAllText($contractPath)

if (-not $original.Contains('"available": true')) {
    throw "No se encontró el campo esperado para crear la incompatibilidad."
}

try {
    $broken = $original.Replace('"available": true', '"isAvailable": true')
    [IO.File]::WriteAllText($contractPath, $broken)

    & (Join-Path $root "gradlew.bat") ":catalog-service:test" "--tests" `
        "com.example.catalog.CatalogContractVerificationTest" "--console=plain" "--no-daemon"
    $verificationExitCode = $LASTEXITCODE

    if ($verificationExitCode -eq 0) {
        throw "La verificación pasó, pero debería fallar con el contrato incompatible."
    }

    Write-Host "OK: la verificación falló como se esperaba (exit code $verificationExitCode)."
}
finally {
    [IO.File]::WriteAllText($contractPath, $original)
    Write-Host "Contrato original restaurado: $contractPath"
}

exit 0