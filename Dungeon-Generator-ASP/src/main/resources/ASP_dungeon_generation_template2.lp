
%% Grid size
room(0..${GRID_WIDTH}, 0..${GRID_HEIGHT}) .

%% Amount of possible overall doors wished for the level
${DOORS_AMOUNT_MIN}{ door(room(X1,Y1), room(X2,Y2)) : room(X1,Y1), room(X2,Y2), X1-X2 <= 1, X2-X1 <= 1, Y1-Y2 <= 1, Y2-Y1 <= 1 }${DOORS_AMOUNT_MAX} .

#const maxDist = 40 .


% every room is reachable from itself
reachable(room(X,Y),room(X,Y)) :- room(X, Y) .

%% Predefined doors if desired
${PREDEFINED_DOORS}


%% Predefined blocking walls if desired
${PREDEFINED_WALLS}


%% Recursive definition of distance from start point
% dist via door north
dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y), D+1) :- not dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y),D-1), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), MID_X=TO_X, MID_Y+1=TO_Y ,   dist(room(FROM_X, FROM_Y), room(MID_X, MID_Y), D) , D <maxDist.

% dist via door south
dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y), D+1) :- not dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y),D-1), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), MID_X=TO_X, MID_Y-1=TO_Y ,   dist(room(FROM_X, FROM_Y), room(MID_X, MID_Y), D) , D <maxDist.

% dist via door west
dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y), D+1) :- not dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y),D-1), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), MID_X-1=TO_X, MID_Y=TO_Y ,   dist(room(FROM_X, FROM_Y), room(MID_X, MID_Y), D) , D <maxDist.

% dist via door east
dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y), D+1) :- not dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y),D-1), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), MID_X+1=TO_X, MID_Y=TO_Y ,   dist(room(FROM_X, FROM_Y), room(MID_X, MID_Y), D) , D <maxDist.


%% doors can only lead to the neighbour field, ie. X XOR Y coordinate differ exactly by 1
:- door(room(X1,Y1), room(X2,Y2)), room(X1,Y1), room(X2,Y2) , X1-X2 > 1 .
:- door(room(X1,Y1), room(X2,Y2)), room(X1,Y1), room(X2,Y2) , X1-X2 < -1 .
:- door(room(X1,Y1), room(X2,Y2)), room(X1,Y1), room(X2,Y2) , Y1-Y2 > 1 .
:- door(room(X1,Y1), room(X2,Y2)), room(X1,Y1), room(X2,Y2) , Y1-Y2 < -1 .
:- door(room(X1,Y1), room(X2,Y2)), room(X1,Y1), room(X2,Y2) , Y1 != Y2 , X1 != X2 .
:- door(room(X1,Y1), room(X2,Y2)), room(X1,Y1), room(X2,Y2) , Y1 = Y2 , X1 = X2 .

%% we always have bidirectional doors:
door(room(X1,Y1), room(X2,Y2)) :- door(room(X2,Y2), room(X1,Y1)), Y1+1=Y2, X1 = X2.
door(room(X1,Y1), room(X2,Y2)) :- door(room(X2,Y2), room(X1,Y1)), Y1-1=Y2, X1 = X2.
door(room(X1,Y1), room(X2,Y2)) :- door(room(X2,Y2), room(X1,Y1)), Y1=Y2, X1+1 = X2.
door(room(X1,Y1), room(X2,Y2)) :- door(room(X2,Y2), room(X1,Y1)), Y1=Y2, X1-1 = X2.

printDoor(room(X1, Y1), room(X2, Y2)) :- door(room(X1, Y1), room(X2, Y2)) , X1 = X2 + 1, Y1 = Y2 .
printDoor(room(X1, Y1), room(X2, Y2)) :- door(room(X1, Y1), room(X2, Y2)) , X1 = X2, Y1 = Y2 + 1 .



%#show edge/3.
#show door/2.
#show locationPosition/2.
#show path/5.
#show dist/3.
#show printDoor/2 .

