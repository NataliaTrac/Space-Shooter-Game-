#Welcome to the Space Invaderos, an exciting arcade-style game built in Java. Navigate through the cosmos, engage in thrilling battles against alien forces, and strive for the high score in this action-packed adventure.

# Features
- **Engaging combat mechanics -** Take on hordes of alien enemies, avoiding their attacks while aiming to take them down with your ship's weapons.
- **Dynamic levels -** Progress through various levels each with increasing difficulty and unique challenges.
- **Customizable options -** Adjust the game settings to tailor the gameplay experience to your liking, including sound effects and difficulty levels.
- **Score tracking -** Compete for the top spot in the rankings by achieving the highest score possible, with a dedicated system for tracking and displaying player scores.
- **Intuitive user interface -** Easily navigate through the game's menus, access settings, and check rankings with a user-friendly interface.

# Gameplay
Navigate your spacecraft using the **arrow keys** and use the **spacebar** to shoot at the alien enemies. Your goal is to survive each level by avoiding or destroying meteors and enemy ships, all while managing your ship's health and ammunition. Strategy and quick reflexes are key to advancing through the game and achieving high scores.

# Development
## Classes Overview
The game's functionality is divided across several classes, each responsible for specific aspects of the game:

- **Main -** Initializes the game window and starts the game loop.
- **Obiekt -** Serves as the base class for all game objects, including the player ship and enemies.
- **Options -** Handles game settings and preferences.
- **Player -** Manages the player's spaceship, including movement and shooting mechanics.
- **Ranking -** Keeps track of high scores and displays them to the player.
- **Shot -** Controls the projectiles fired by the player.
- **UI -** Manages the game's user interface, including menu screens and game over screens.
- **WARTOŚCI -** Contains global constants used throughout the game, such as dimensions and speed settings.
- **Alien -** Defines the behavior and attributes of alien enemies.
- **Game -** Acts as the central hub for game logic, managing game states, level progression, and interactions between game objects. It orchestrates the game loop, including updating game mechanics and rendering the game objects to the screen.
- **HighScore -** Manages the high score data, including saving, loading, and displaying high scores. This class ensures that player achievements are recorded and retrievable, providing a competitive edge to the game experience.

## High Score Management
The HighScore class enhances the game by adding a competitive layer, encouraging players to surpass their previous achievements. It is designed to:

- **Save High Scores -** Utilizing Java's I/O capabilities to write high scores to a file.
- **Load High Scores -** Reads the high score file at the start of the game, ensuring scores are persistent between sessions.
- **Display High Scores -** Integrates with the game's UI to show current high scores, giving players targets to beat.
