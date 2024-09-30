# WaifuHax V3

![Static Badge](https://img.shields.io/badge/Minecraft-1.21.1-blue)

---

WaifuHax is a basic, easily hackable utility client made for Minecraft 1.21.1.

Link to the official wiki: [WaifuHax's wiki](https://git.someboringnerd.xyz/MinecraftAnarchy/WaifuHaxV3-Public/wiki/)

## Download

There are two ways of downloading the client

### Compiling the Mod Yourself

<details>
<summary>Click to expand</summary>

Compiling the client from source is a good practice if you want to audit the source code to make sure I didn't put shady code in the releases or if you want to hack your own code in (something I greatly encourage).

| Instruction | Command |
|-------------|---------|
| Clone this repository: | `git clone https://git.someboringnerd.xyz/MinecraftAnarchy/WaifuHaxV3-Public` |
| cd into the folder: | `cd WaifuHaxV3-Public` |
| compile the client (Unix): | `./gradlew build` |
| compile the client (Windows (ew)): | `.\gradlew.bat build` |

The compiled build will be in the `build/libs` folder as `waifuhax-3.0.0.jar`.

note : if you use Nix in any capacity, a shell.nix is present to manage dependencies without issues

</details>

### Download from Releases

<details>
<summary>Click to expand</summary>

If this is too hard, you can just use the release tab on the client's upstream repo. I would have put in place some CI/CD pipeline of some kind but I'm too lazy to do so. That's the price to pay if you cant compile code yourself.

| Step                                                                                                                                    |
|-----------------------------------------------------------------------------------------------------------------------------------------|
| Go to the [WaifuHax's release page, git.someboringnerd.xyz](https://git.someboringnerd.xyz/MinecraftAnarchy/WaifuHaxV3-Public/releases) |
| Click on `waifuhax-3.0.0-all.jar`                                                                                                       |
| Put the jar file in your mods folder, no additional mods are required                                                                   |

Easy as pie, but also the riskiest option of them all, as you can't even check if I published malicious code or not (without decompiling the mod, at least). Really if you have even the slightest idea of what you are doing, please clone the repo, audit the source code and build it yourself, this shitty client is not worth the risk.

</details>

## Support

Please do not contact me on Discord if you have troubles with the client, I will likely ignore you.

## Features

WaifuHax has a lot of features, mainly integration with RusherHack, Future, and Meteor, allowing you to sync your friend list across all three clients, assuming you own at least one of them.

The default key for opening the ClickGui is RSHIFT and is not modifiable yet.

The prefix for commands is `!` and is not modifiable yet.

## Credits

---

WaifuHax V3 takes its roots in ForgeHax's ideas. It is designed as a "for power user" client meant to be modified and used as a side client prioritizing function over a fancy ClickGui.

While the client uses the same GUI library as [Haiku](https://github.com/vil/haiku), and even "reuse" (blatantly pasted) some code, both clients are two separate projects, and WaifuHax does not use Haiku as a base (we initially tried, but the way we do the rendering of Imgui changed too much from Haiku to be worth using, as it would have required rewriting most of the original code base). Haiku was definitely a heavy inspiration for using Imgui over making our own ClickGui, though.

Libraries used:

| Library | Description |
|---------|-------------|
| Meteor-Orbit | Simple and efficient event system |
| Meteor-RPC | Simple way to make a Discord Rich Presence system |
| pircbotx | A quick and dirty way to make an IRC client (meant to be used to make IRC bots) |
| Spair's ImGUI binding | Fast and useful Imgui binding for Java |
| LuaJ | interpreter for Lua written in pure Java |
| Project Lombok | Helps eliminate a lot of boilerplate code. You may need to install the plugin for your IDE, or it will throw errors (not required for compilation) |

## Todo

| Task |
|------|
| Rewrite autoframedupe |
| Rewrite the entire GUI system |
| Finish planned modules |
