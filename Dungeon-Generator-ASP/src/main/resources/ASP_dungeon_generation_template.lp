
%% Grid size
room(1..${GRID_WIDTH}, 1..${GRID_HEIGHT}) .

%% Starting Point and Goal Point
#const start = room(${START_X}, ${START_Y}).
#const end = room(${EXIT_X}, ${EXIT_Y}).
#const maxDist = 40 .

%% Amount of possible overall doors wished for the level
${DOORS_AMOUNT_MIN}{ door(room(X1,Y1), room(X2,Y2)) : room(X1,Y1), room(X2,Y2) }${DOORS_AMOUNT_MAX}.

%% Required minimum distance from start to goal
${SHORTEST_EXIT_PATH}

%:- dist(start, end, 10) .
%:- dist(start, end, 9) .
%:- dist(start, end, 8) .
%:- dist(start, end, 7) .
%:- dist(start, end, 6) .
%:- dist(start, end, 5) .
%:- dist(start, end, 4) .
%:- dist(start, end, 3) .
%:- dist(start, end, 2) .
%:- dist(start, end, 1) .



%% Predefined doors if desired
${PREDEFINED_DOORS}
%door(room(1,1), room(1,2)) .
%door(room(1,2), room(2,2)) .
%door(room(2,1), room(2,2)) .
%door(room(2,2), room(3,2)) .
%door(room(3,2), room(3,3)) .
%door(room(2,2), room(2,1)) .
%door(room(2,1), room(3,1)) .
%door(room(3,1), room(3,2)) .

%% Predefined blocking walls if desired
${PREDEFINED_WALLS}
%not door(room(1,1), room(2,1)) .
%not door(room(2,1), room(1,1)) .
%not door(room(1,2), room(2,2)) .
%not door(room(2,2), room(1,2)) .
%not door(room(2,3), room(3,3)) .
%not door(room(3,3), room(2,3)) .

%not door(room(2,2),room(3,2)) .


%% doors can only lead to the neighbour field, ie. X XOR Y coordinate differ exactly by 1
:- door(room(X1,Y1), room(X2,Y2)), room(X1,Y1), room(X2,Y2) , X1-X2 > 1 .
:- door(room(X1,Y1), room(X2,Y2)), room(X1,Y1), room(X2,Y2) , X1-X2 < -1 .
:- door(room(X1,Y1), room(X2,Y2)), room(X1,Y1), room(X2,Y2) , Y1-Y2 > 1 .
:- door(room(X1,Y1), room(X2,Y2)), room(X1,Y1), room(X2,Y2) , Y1-Y2 < -1 .
:- door(room(X1,Y1), room(X2,Y2)), room(X1,Y1), room(X2,Y2) , Y1 != Y2 , X1 != X2 .
:- door(room(X1,Y1), room(X2,Y2)), room(X1,Y1), room(X2,Y2) , Y1 = Y2 , X1 = X2 .

%% we always have bidirectional doors:
door(A, B) :- door(B, A) .

%% Search start distance
dist(start, start, 0) .

%% Recursive definition of distance from start point
%dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y),D+1) :- not dist(room(FROM_X, FROM_Y), room(TO_X, TO_Y),D-1),   dist(room(FROM_X, FROM_Y), room(MID_X, MID_Y), D) , D <25, door(room(MID_X, MID_Y), room(TO_X, TO_Y)). 
dist(FROM, TO, D+1) :- not dist(FROM,TO,D-1),   dist(FROM, MID, D) , D <maxDist, door(MID, TO).


reachable(start, TO) :- door(start, TO) .
reachable(FROM, TO) :- reachable(FROM, MID), door(MID, TO).

:- not reachable(start, end) .
:- door(FROM, TO), not reachable(start, TO) .






printDoor(room(X1, Y1), room(X2, Y2)) :- door(room(X1, Y1), room(X2, Y2)) , X1 >= X2, Y1 >= Y2 .


%#show edge/3.
#show door/2.

#show path/5.
#show dist/3.
#show printDoor/2 .

