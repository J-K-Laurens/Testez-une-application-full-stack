# Configuration de l'environnement pour le projet
Write-Host "Configuration de l'environnement..." -ForegroundColor Cyan

# Java 11
$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-11.0.29.7-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Charger les variables d'environnement systeme pour NVM
$env:NVM_HOME = [System.Environment]::GetEnvironmentVariable("NVM_HOME","Machine")
$env:NVM_SYMLINK = [System.Environment]::GetEnvironmentVariable("NVM_SYMLINK","Machine")
if ($env:NVM_HOME) {
    $env:PATH = "$env:NVM_HOME;$env:NVM_SYMLINK;$env:PATH"
}

# Node.js 16 (via NVM)
$nvmOutput = nvm use 16.20.2 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "OK Node.js 16.20.2 active" -ForegroundColor Green
} else {
    Write-Host "ATTENTION NVM non disponible" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "=== Versions installees ===" -ForegroundColor Green

# Java
Write-Host "Java: " -NoNewline -ForegroundColor Cyan
$javaVersion = java -version 2>&1 | Select-Object -First 1
Write-Host $javaVersion

# Node.js
Write-Host "Node: " -NoNewline -ForegroundColor Cyan
try {
    $nodeVersion = node --version 2>&1
    Write-Host $nodeVersion
} catch {
    Write-Host "Non disponible" -ForegroundColor Yellow
}

# npm
Write-Host "npm: " -NoNewline -ForegroundColor Cyan
try {
    $npmVersion = npm --version 2>&1
    Write-Host $npmVersion
} catch {
    Write-Host "Non disponible" -ForegroundColor Yellow
}

# Angular CLI
Write-Host "Angular CLI: " -NoNewline -ForegroundColor Cyan
try {
    $ngVersion = ng version 2>&1 | Select-String "Angular CLI" | Select-Object -First 1
    Write-Host $ngVersion
} catch {
    Write-Host "Non disponible" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Environnement pret!" -ForegroundColor Green
