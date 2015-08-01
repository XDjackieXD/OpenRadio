# OpenRadio
A Little OpenComputers addon which adds laser links and (planned) satellites with radios, antennas and other high frequency stuff :3

## Current status
* Lasers check if they have an opponent via regular entities (currently visible for debugging. 1 entity per second).
* I need better textures for the two laser tubes (Laser item tier 2 and 3) and recipes for all three laser tiers.
* Basic AE2 integration is working (DSP tier 3 is required for the AE Encoder to work)
* Maximum distance:
 * Laser tier 1: 256 blocks
 * Laser tier 2: 512 blocks
 * Laser tier 3: 1024 blocks

## Build instructions
1. Create a folder called `lib` and put the dev-version of Applied Energistics 2 in there (at least rv2-stable-10)
2. Setup the workspace: `./gradlew setupDecompWorkspace --refresh-dependencies`
3. Compile the mod: `./gradlew build` or setup the IDE files for development: `./gradlew idea`/`./gradlew eclipse`
