# Bop-It
Bop-It is a multiplayer, android game made for the Course "623.951 (23S) App Development". It emulates the functionally of the children’s toy with the same name, by asking players to quickly react to commands and execute them as fast as possible. 

## Setup
In the main menu, the players can choose a name for themselves and choose whether they want to host a game or join another player.

![image](https://github.com/Benjamin-Dippold/Bop-It/assets/127213239/998e6367-0885-43b1-b2f0-b069439b783b)

If the player chooses to host a game, their current IP address will be displayed. If the player chooses to join a game, they will be prompted to enter the IP address of the host. In both cases, there is still an option to return to the menu.

![image](https://github.com/Benjamin-Dippold/Bop-It/assets/127213239/d4564407-c102-4cd8-a1cb-c79effe62e1e)
![image](https://github.com/Benjamin-Dippold/Bop-It/assets/127213239/f75060ee-6a22-46cd-909c-decb8e544742)


After the players are connected, the host is taken to a settings-screen, while the client is taking to a waiting screen. The settings to be adjusted are the points a player must earn to win the game and to switch between normal and hard mode (for more see "Hard Mode").

![image](https://github.com/Benjamin-Dippold/Bop-It/assets/127213239/3c5d0d9b-85d7-49df-be9f-2b8282981fec)
![image](https://github.com/Benjamin-Dippold/Bop-It/assets/127213239/a8223304-b3ea-4f20-a91f-1b7862b588bd)

When the host confirms their settings, both players will be sent to the waiting screen and the game will begin. After a randomized amount of time (between 2 and 5 seconds) both players will be given a new prompt to complete. Players will return to this screen after every prompt to see their and their opponent’s performance, with their own performance always being displayed on the top and their opponents on the bottom. The names entered on the main menu screen will also be used here. If a player has not entered a name, they will be displayed as "Player" for themselves and "Enemy" for the opponent.

![image](https://github.com/Benjamin-Dippold/Bop-It/assets/127213239/73467684-0aa6-4238-81f1-357609432856)

If one of the players reaches the point total, that was set on the settings screen, they win the game, and both players are sent to the score screen. Here a history of all prompts given is displayed with information on the times each player took and whether it was a fake prompt in hard mode. The host also has a button to play again, which will send the host back to the settings screen and with that the ability to start a new game from there.

![image](https://github.com/Benjamin-Dippold/Bop-It/assets/127213239/383a8217-7b1f-4dc4-b31d-bbded113f447)

## List of Prompts
 - **Tap it!**: Tap on the screen
 - **Double Tap it!**: Tap twice on the screen
 - **Hold it!**: Tap and hold on the screen
 - **Fling it!**: Perform a fling gesture
 - **Pinch it!**: Perform a pinch gesture
 - **Zoom it!**: Perform a zoom gesture
 - **Shake it!**: Shake the device
 - **Turn it!**: Rotate the device
 - **Solve it!**: Solve a simple arithmetic question (multiple choice)
 - **Throw it!**: Throw the pone into the air
 - **Volume up!**: Press the increase volume button on the device
 - **Volume down!**: Press the decrease volume button on the device
 - **Brightness Down!**: Lower the brightness on your device (if the brightness is already on the lowest level, it must be increased and then decreased)
 - **Brightness Up!**: Up the brightness on your device (if the brightness is already on the highest level, it must be decreased and then increased)
 - **Point to north!**: Rotate the device, so it's pointing north

## Scoring
When a prompt begins, both devices start their own independent timers. When a player finishes a prompt that time is sent to the server. The server will then wait another second for the other player to send their time. The server checks these two times to determine who gets the point, which eliminates the disadvantage that latency might give one player over the other. If the server does not receive the time from the second player, they are marked DNF (Did Not Finish) and the first player to send their prompt will be given the point. If a player spends more than 5 seconds on a prompt (or give a wrong answer on "Solve it!") they will be marked as "Failed" and not given the point. If both players "Failed", or both players get the exact same time, neither gets a point.

## Hard Mode
Hard Mode enables the chance that each prompt could be "fake". When a player receives a fake prompt, the voice line that introduces the prompt will be switched out with a different voice. Which voice indicates a real and fake clip can be previewed on the settings screen. A fake prompt will swap the Win- and Lose-Conditions of a prompt. If a player completes a fake prompt, they will be marked as "Failed", which will cause their opponent to get a point. If both players fail within one second, neither is given the point. If a player waits out the 5 seconds it takes to normally fail a prompt, they will receive a time of 5 seconds and will be scored normally.
