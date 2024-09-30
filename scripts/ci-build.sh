#!/usr/bin/env bash
apt-get update
apt-get install -y git

branch=$(git rev-parse --abbrev-ref HEAD)
echo "$branch"

if [ "$branch" == "main" ] || [ "$branch" == "HEAD" ]; then
    apt-get install -y openjdk-17-jdk
    chmod +x ./gradlew && ./gradlew build
fi