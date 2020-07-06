# Corners

Corners is a original game created by myself and [Justin Goodman](https://jugoodma.github.io/).
It is a two player game which can be played with pen and paper, however there is a [web application](https://github.com/CliffBakalian/Corners) available and of course this android application.  

### API Reference

Minimum SDK: 23 Target SDK 29

## APK

For those that just want the apk, it can be found [corners.apk](./corners.apk)  

## How to Play

#### Board

The board can be any *n*x*n* board where *n* is an odd number greater than 1. 
However we found a 3x3 board is too small to play an actual fun game so we suggest to 
play with a 5x5 board.   

#### Pieces

Each player has 4 pieces,  
![pieces] (./img/pieces.png)  
Each piece has a certain orientation which it must be played. 
The orientation of the last played piece dictates where the next piece can be played. 
Once a player places a piece down, they cannot place that piece down again until all 
other pieces have been played. 
After the player has placed all 4 pieces down, they get one of each piece again.

### Gamplay

Players take turns placing down pieces onto the game board. 
The first player must play in the center square.
The next player then can place thier piece down in the same row or same column 
as the previous piece in the direction at which a piece 'points.'

For example:  
![piece] (./img/piece.png)  
This piece 'points' down and to the right. So the next piece which can be placed
must be placed on the sam row to the right or the same column below this piece.  

![example] (./img/board.png)   

Do to how past piece influece the next piece, there are 4 illegal moves:  

![illegal] (./img/illegal.png)  

### Ending the Game

The game ends when a player cannot place a piece on the board. Either there is no viable squares or 
the player would be forced to make one of the four illegal moves. 

The winner is the one who 'cornered' thier opponent. 
