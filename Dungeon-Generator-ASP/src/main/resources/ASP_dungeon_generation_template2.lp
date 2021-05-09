
%% Grid size
room(0..${GRID_WIDTH}, 0..${GRID_HEIGHT}) .

%% Amount of possible overall doors wished for the level
${DOORS_AMOUNT_MIN}{ door(room(X1,Y1), room(X2,Y2)) : room(X1,Y1), room(X2,Y2), X1-X2 <= 1, X2-X1 <= 1, Y1-Y2 <= 1, Y2-Y1 <= 1 }${DOORS_AMOUNT_MAX} .

#const maxDist = 40 .


% every room is reachable from itself
reachable(room(X,Y),room(X,Y)) :- room(X, Y) .

% each location need to be positioned
1{locationPosition(LOC, room(X, Y)) : room(X, Y)}1 :- location(LOC) .

% their can only be one location in a room
:- location(LOCA),  location(LOCB), LOCB != LOCA, locationPosition(LOCA, LOCPOSA), locationPosition(LOCB, LOCPOSB), LOCPOSA = LOCPOSB .

% distribute locations
adjacentLocation(location(LOCA), location(LOCB)) :- location(LOCA), location(LOCB) , locationPosition(LOCA, room(XA,YA)), locationPosition(LOCB, room(XB,YB)), door(room(XA,YA), room(XB,YB)).
#minimize{adjacentLocation(location(LOCA), location(LOCB)): location(LOCA), location(LOCB)}.

% maximize locations at dead ends
#maximize{locationPosition(LOC, POS): oneDoors(POS), location(LOC)} .

% at each dead end we need some location
1{locationPosition(LOC ,  ROOM) : location(LOC)} 1 :- oneDoors(ROOM) .

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

%key("Key1") .

%1{lock(door(room(X1, Y1), room(X2, Y2)), K) : key(K), door(room(X1, Y1), room(X2, Y2)), room(X1, Y1), room(X2, Y2) }   1 .
%locked(door(room(X2, Y2), room(X1, Y1)), K) :- lock(door(room(X1, Y1), room(X2, Y2)), K) .
%locked(door(room(X2, Y2), room(X1, Y1)), K) :- locked(door(room(X1, Y1), room(X2, Y2)), K) .
%:- locked(door(room(X2, Y2), room(X1, Y1)), K), not door(room(X2, Y2), room(X1, Y1)) .


%reachableWithoutKey(revealmapshrinePos, TO, KEY) :- door(revealmapshrinePos, TO), not locked(door(revealmapshrinePos, TO), KEY), key(KEY) .

%reachableWithoutKey(revealmapshrinePos, room(TO_X, TO_Y), KEY) :- reachableWithoutKey(revealmapshrinePos, room(MID_X, MID_Y), KEY), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), not locked(door(room(MID_X, MID_Y), room(TO_X, TO_Y)), KEY), key(KEY), MID_X=TO_X, MID_Y+1=TO_Y .
%reachableWithoutKey(revealmapshrinePos, room(TO_X, TO_Y), KEY) :- reachableWithoutKey(revealmapshrinePos, room(MID_X, MID_Y), KEY), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), not locked(door(room(MID_X, MID_Y), room(TO_X, TO_Y)), KEY), key(KEY), MID_X=TO_X, MID_Y-1=TO_Y .
%reachableWithoutKey(revealmapshrinePos, room(TO_X, TO_Y), KEY) :- reachableWithoutKey(revealmapshrinePos, room(MID_X, MID_Y), KEY), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), not locked(door(room(MID_X, MID_Y), room(TO_X, TO_Y)), KEY), key(KEY), MID_X+1=TO_X, MID_Y=TO_Y .
%reachableWithoutKey(revealmapshrinePos, room(TO_X, TO_Y), KEY) :- reachableWithoutKey(revealmapshrinePos, room(MID_X, MID_Y), KEY), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), not locked(door(room(MID_X, MID_Y), room(TO_X, TO_Y)), KEY), key(KEY), MID_X-1=TO_X, MID_Y=TO_Y .

%:- reachableWithoutKey(revealmapshrinePos, levelexitPos, "Key1") .
%:- reachableWithoutKey(revealmapshrinePos, scoutshrinePos, "Key1") .


#show door/2.
#show dist/3.
#show printDoor/2 .
#show locked/2.
#show reachableWithoutKey/3.
#show reachable/2.
#show locationPosition/2.

