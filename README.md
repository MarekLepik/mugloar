# High Score Game Log
- End of turn 36167
  - Gold = 189772
  - Lives = 4
  - Score = 10,969,172
  - Game ID: 08K0XmjY
    
- End of turn 9151: Gold = 85531068, Lives = 4, Score = 87262068 Game ID: p7VFBsGs


# General bot playing loop
Description of the general bot playing loop
## Loop
After initialization, the bot enters a loop where it continuously reads the game state, makes decisions, and outputs actions until the game ends.
1. Fetches Task list
2. Fetches Shop list
3. Updates bot state with current game state: Lives and gold from last turn decision
4. Enter decision service
   - Decides which task to choose
   - Decides which items to buy from the shop
5. Based on previous both decisions:
   - Checks if user needs healing (prioritizes)
   - Checks selected task probability
   - If task simple enough, executes it before buying items
   - Otherwise, tries to buy item - restarts the loop since buying an item may change the task decision (chances)
   - Fallback to task if no items can be bought
6. Returns decision to game engine
7. Action is made
8. Repeat until game ends