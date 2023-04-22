# Jetpack Joyride README.md

![Diagram]("D:\Downloads\JetpackJoyrideDiagram.png")

This is a simple Jetpack Joyride clone built using JavaFX. It is a side-scrolling game where the player controls the
main character, Barry, who uses a jetpack to navigate through a series of obstacles, collect coins, and avoid missiles.
The game continues until Barry collides with an obstacle or a missile, at which point the game is over.

## Classes

The project consists of the following classes:

- **Gameplay**: This class represents the main gameplay logic and includes the functionality to manage the game loop,
  update game objects, handle the creation and management of various game objects, track and update the game state,
  handle collisions, and control the background and its scrolling.
- **GameOver**: This class represents the game over screen displayed when the player loses. It provides options to
  restart the game or quit.
- **Barry**: This class is responsible for handling Barry's movements, image updates, and animations.
- **Coin**: This class represents the coins in the game, handles animations, and manages coin patterns.
- **JoyrideController**: This class is the main controller for the game, handling game state updates, meter counter, and
  coin counter display.
- **Missile**: This class represents the missiles in the game, handling their animations and hitboxes.
- **SoundManager**: This class manages the various audio clips used in the game, such as coin collection sounds, warning
  sounds, and missile launch sounds.
- **Zapper**: This class represents the zapper obstacles in the game, handling their images, animations, and hitboxes.
- **BackgroundStyle**: This enum is used to define the different background styles for the game, such as SECTOR,
  VOLCANO, and LAB.
- **JoyrideApplication**: This class is the main entry point for the JavaFX application, initializing the game window
  and loading the required resources.

## How to Run

To run the game, compile and run the main class containing the JavaFX application. The game window will appear, and you
can start playing.

## Controls

Use the spacebar to control Barry's jetpack. Press and hold the spacebar to make Barry ascend, and release it to make
him descend.

## Upcoming Features

- Power-ups and additional obstacles
- More detailed scoring system
- Multiple levels with varying difficulty
- Saving and loading of high scores
- Improved graphics and animations
