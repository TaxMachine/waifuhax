# build the game and rename the executable
./gradlew build

if [ $? -ne 0 ]; then
  exit 1
fi

mv ./build/libs/waifuhax-3.0.0.jar ~/.local/share/PrismLauncher/instances/1.21.1/.minecraft/mods/

prismlauncher -l 1.21.1
