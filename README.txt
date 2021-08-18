Here is my finished summer project! (I met with Allen Wang to design and plan it, and he helped setup some of my population classes but I did the rest)

I named it Dungeon Simulator because the whole purpose of it is that it simulates how a type of Artificial Intelligence would learn to beat a top-down "dungeon" or "maze" game.

The objective for the AI is pretty simple. They must navigate through the level and reach the goal. They can only travel in the level's designated boundaries.
They also can't pass through walls and die when they hit any of the two types of enemies.

The AI learns using a genetic algorithm. They begin by knowing absolutely nothing about the level, simply having a "brain" of many random steps. An important note is
that they have no idea where the goal is. As they wander aimlessly, some will, by luck, get closer to the goal. After a little bit, a fitness will be calculated solely
based on how close it is to the goal. The higher the fitness, the more often its randomly generated steps are prioritized in the creation of the next generation of AI.
This algorithm means that over time, their steps will lead them to the goal. After it reaches the goal, the AI will also begin to improve the path they take and they
will end up taking fewer steps. The algorithm I created also contains a few edits to help it deal with different scenarios.

First, it contains mutations. These help the AI since the prior paths that get the AI closer to the goal are not always perfect and the moves can be altered to be more
efficient. Next, the algorithm not only considers the highest fitness but also the runners up. This will help in case the best performing AI hits a corner and refuses
to get out of it while runners up begin to learn to go around it. Finally, the algorithm uses an incremental learning system. They start with a very limited amount of
steps and gradually get access to more. This forces the AI to perfect the first few steps it gets before being able to try out more. This also decreases the time it
takes for the AI to learn. 

Once I thought the AI learned pretty well, I began to think how I could make the project more fun, both to code and to test. So I made it a "sandbox" where the user can
make their own level. This is where the advanced customization settings come in. Users can position walls and enemies whereever they like. Since I already made the
level completely customizable, I decided to make the algorithm customizable too. Users can adjust the values of the algorithm to fit their level i.e. the number of AI
per generation or the mutation rate.

I'm personally quite proud of the level saving system. Being able to load infinitely unique levels was an idea that I could not resist implementing. After that,
the rest of the features were just to improve the users experience as they were designing their levels such as the player testing mode and simulation speed slider.




HOW TO USE THE APP:
All the buttons and boxes have labels and also descriptive tool tips. Just hover over anything to learn more about it.
Click on a sprite in the simulation window to select it. You can then customize many settings. After selecting a sprite, you can drag it to a new location to reposition it.
Whenever the simulation is paused, the AI, the enemies, and your square will be frozen but you can still customize things.
New sprites can be added using the button panel on the right.
Information about the AI's progress is displayed at the top.




Sample levels to be loaded (Paste the code into the box in the bottom right and press enter):

SX66Y306ZGX487Y319ZBX52Y167W460H300ZVX148Y231W360H20ZVX140Y166W20H80ZVX152Y388W360H20ZVX141Y386W20H80ZVX325Y274W20H90ZEM259N274J258I372W10H10D3ZEM470N262J393I372W10H10D4ZEM393N262J467I377W10H10D4ZPC217K311W10H10D1R65ZPC318K318W10H10D2R65

SX274Y457ZGX149Y239ZBX84Y77W400H400ZVX190Y174W20H90ZVX86Y340W200H20ZVX82Y204W115H20ZVX398Y169W20H80ZVX210Y397W20H80ZVX270Y250W20H170ZVX189Y172W230H20ZVX349Y234W135H20ZVX351Y253W20H120ZEM235N215J365I214W10H10D1ZPC317K290W10H10D2R50