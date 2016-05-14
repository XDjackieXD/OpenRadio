# OpenRadio
A Little OpenComputers addon which adds laser links and (planned) satellites with radios, antennas and other high frequency stuff :3

## Current status
* Lasers check if they have an opponent via regular entities (1 entity per second).
* (Basic AE2 integration is working (DSP tier 3 is required for the AE Encoder to work)) 1.7.10 only. Blame AlgorithmX2 ^^
* Lasers need all inventory slots filled with the appropriate items (different tiers currently have no effect except for the laser tube and the DSP)
* Lenses can be stacked (placed after each other)
 * Maximum Multiplier = (Diamond Lens Multiplier+1)*2
* Maximum distance:
 * Laser tier 1: 64 blocks
 * Laser tier 2: 128 blocks
 * Laser tier 3: 256 blocks
* Lenses distance multiplier (without lens multiplier = 1):
 * Glass Lens: +1
 * Quartz Infused Lens: +3
 * Shaped Diamond Lens: +7
* Power usage:
 * Laser tier 1: 25 RF/tick
 * Laser tier 2: 50 RF/tick
 * Laser tier 3: 100 RF/tick
 * (AE Encoder multiplies this by 10) 1.7.10 only. Blame AlgorithmX2 ^^

## TODO
* I need better textures for the two laser tubes (Laser item tier 2 and 3) and recipes for all three laser tiers.
* Rendering of laser beams. Quite hard to do. Help and ideas are appreciated!
* add all the fancy stuff from Ideas.txt :3

## Build instructions
1. Setup the workspace: `./gradlew setupDecompWorkspace --refresh-dependencies`
2. Compile the mod: `./gradlew build` or setup the IDE files for development: `./gradlew idea` or `./gradlew eclipse`