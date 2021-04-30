
%% Grid size
room(0..${GRID_WIDTH}, 0..${GRID_WIDTH}) .

%% Amount of possible overall doors wished for the level
${DOORS_AMOUNT_MIN}{ door(room(X1,Y1), room(X2,Y2)) : room(X1,Y1), room(X2,Y2), X1-X2 <= 1, X2-X1 <= 1, Y1-Y2 <= 1, Y2-Y1 <= 1 }${DOORS_AMOUNT_MAX} .

#const maxDist = 40 .
%#const start = "START" .
%location(start) .


%locationPosition(start, room(${START_X}, ${START_Y})) .
%locationPosition("EXIT", room(4, 1)) .

% each location has exactly one position
1{ locationPosition(LOC, room(X1,Y1)) : room(X1,Y1) }1 :- location(LOC) .

% each room can only have one location at most
%LOC1 = LOC2 :- location(LOC1), location(LOC2), locationPosition(LOC1, room(X1,Y1)), locationPosition(LOC2, room(X1,Y1)) .

% each location has to be reachable from the start point
%:- not reachable(START, POS) , locationPosition(LOC, POS), location(LOC), locationPosition(start ,  START) .


% every room is reachable from itself
reachable(room(X,Y),room(X,Y)) :- room(X, Y) .

% start is the reachable starting reference point
%reachable(START, TO) :- door(START, TO), locationPosition(start ,  START) .

%% transitive reachability using doors
% door north
reachable(room(FROM_X, FROM_Y), room(TO_X, TO_Y)) :- reachable(room(FROM_X, FROM_Y), room(MID_X, MID_Y)), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), MID_X=TO_X, MID_Y+1=TO_Y.

% door south
reachable(room(FROM_X, FROM_Y), room(TO_X, TO_Y)) :- reachable(room(FROM_X, FROM_Y), room(MID_X, MID_Y)), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), MID_X=TO_X, MID_Y-1=TO_Y.

% door west
reachable(room(FROM_X, FROM_Y), room(TO_X, TO_Y)) :- reachable(room(FROM_X, FROM_Y), room(MID_X, MID_Y)), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), MID_X+1=TO_X, MID_Y=TO_Y.

% door east
reachable(room(FROM_X, FROM_Y), room(TO_X, TO_Y)) :- reachable(room(FROM_X, FROM_Y), room(MID_X, MID_Y)), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), MID_X-1=TO_X, MID_Y=TO_Y.


%% Predefined doors if desired
${PREDEFINED_DOORS}


%% Predefined blocking walls if desired
${PREDEFINED_WALLS}


%% DEFINITION OF EXIT

%location("EXIT") .




%% Required minimum distance from start to goal
%${SHORTEST_EXIT_PATH}

%:-not dist(START, EXITPOS, 10) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 1) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 2) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 3) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 4) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 5) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 6) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 7) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 8) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 9) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .



%% Search start distance
%dist(START, START, 0) :- locationPosition(start ,  START)  .

%% Recursive definition of distance from start point
% dist via door north
dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y), D+1) :- not dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y),D-1), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), MID_X=TO_X, MID_Y+1=TO_Y ,   dist(room(FROM_X, FROM_Y), room(MID_X, MID_Y), D) , D <maxDist.

% dist via door south
dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y), D+1) :- not dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y),D-1), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), MID_X=TO_X, MID_Y-1=TO_Y ,   dist(room(FROM_X, FROM_Y), room(MID_X, MID_Y), D) , D <maxDist.

% dist via door west
dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y), D+1) :- not dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y),D-1), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), MID_X-1=TO_X, MID_Y=TO_Y ,   dist(room(FROM_X, FROM_Y), room(MID_X, MID_Y), D) , D <maxDist.

% dist via door east
dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y), D+1) :- not dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y),D-1), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), MID_X+1=TO_X, MID_Y=TO_Y ,   dist(room(FROM_X, FROM_Y), room(MID_X, MID_Y), D) , D <maxDist.


% we MUST not have doors to some point that is not reachable from start
:- door(room(FROM_X, FROM_Y), room(TO_X, TO_Y)), FROM_X+1=TO_X,	FROM_Y=TO_Y	,			locationPosition(start ,  START), 	not reachable(START, room(TO_X, TO_Y))  .
:- door(room(FROM_X, FROM_Y), room(TO_X, TO_Y)), FROM_X-1=TO_X,	FROM_Y=TO_Y	,			locationPosition(start ,  START), 	not reachable(START, room(TO_X, TO_Y))  .
:- door(room(FROM_X, FROM_Y), room(TO_X, TO_Y)), FROM_X=TO_X,	FROM_Y+1=TO_Y	,		locationPosition(start ,  START), 	not reachable(START, room(TO_X, TO_Y))  .
:- door(room(FROM_X, FROM_Y), room(TO_X, TO_Y)), FROM_X=TO_X,	FROM_Y-1=TO_Y	,		locationPosition(start ,  START), 	not reachable(START, room(TO_X, TO_Y))  .


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

