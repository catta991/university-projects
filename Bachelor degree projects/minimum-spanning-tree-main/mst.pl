%%% -*- Mode: Prolog -*-

%%% Cattaneo Alessandro 851612
%%% Riva Andrea 838845
%%% Inajjar Adameddine 859260

%%% mst.pl

:- dynamic graph/1.
:- dynamic vertex/2.
:- dynamic arc/4.
:- dynamic heap/2.
:- dynamic heap_entry/4.
:- dynamic vertex_previous/3.
:- dynamic new_arc/4.
:- dynamic vertex_key/3.


new_graph(G) :-
    graph(G),
    !.

new_graph(G) :-
    assert(graph(G)),
    !.

delete_graph(G) :-
    retract(graph(G)),
    retractall(vertex(G, _)),
    retractall(arc(G, _, _, _)).

new_vertex(G, V) :-
    vertex(G, V),
    !.

new_vertex(G, V) :-
    graph(G),
    \+ vertex(G, V),
    assert(vertex(G, V)),
    !.

graph_vertices(G, Vs) :-
    findall(vertex(G, V), vertex(G, V), Vs).

list_vertices(G) :-
    graph(G),
    listing(vertex(G, _)).

new_arc(G, U, V) :-
    graph(G),
    arc(G, U, V, 1),
    !.

new_arc(G, U, V) :-
    graph(G),
    arc(G, V, U, 1),
    !.

new_arc(G, U, V) :-
    graph(G),
    vertex(G, U),
    vertex(G, V),
    assert(arc(G, U, V, 1)),
    !.

new_arc(G, U, V, Weight) :-
    graph(G),
    vertex(G, U),
    vertex(G, V),
    \+ arc(G, U, V, _),
    \+ arc(G, V, U, _),
    !,
    assert(arc(G, U, V, Weight)).

new_arc(G, U, V, Weight) :-
    graph(G),
    change_weight(G, U, V, Weight),
    !.

change_weight(G, U, V, Weight) :-
    arc(G, U, V, OldWeight),
    Weight \= OldWeight,
    !,
    retract(arc(G, U, V, OldWeight)),
    assert(arc(G, U, V, Weight)).

change_weight(G, U, V, Weight) :-
    arc(G, V, U, OldWeight),
    Weight \= OldWeight,
    !,
    retract(arc(G, V, U, OldWeight)),
    assert(arc(G, U, V, Weight)).

change_weight(G, U, V, Weight) :-
    arc(G, V, U, OldWeight),
    Weight == OldWeight,
    !.

change_weight(G, U, V, Weight) :-
    arc(G, U, V, OldWeight),
    Weight == OldWeight,
    !.

graph_arcs(G, Es) :-
    findall(arc(G, U, V, Weight), arc(G, U, V, Weight), Es).

vertex_neighbors(G, V, Ns) :-
    graph(G),
    vertex(G, V),
    findall(arc(G, V, N, Weight), arc(G, N, V, Weight), L1),
    findall(arc(G, V, N, Weight), arc(G, V, N, Weight), L2),
    append(L1, L2, Ns),
    !.

adjs(G, V, Vs) :-
    graph(G),
    vertex(G, V),
    findall(vertex(G, Y), arc(G, V, Y, _), Dx),
    findall(vertex(G, Y), arc(G, Y, V, _), Sx),
    append(Dx, Sx, Vs),
    !.

list_arcs(G) :-
    graph(G),
    listing(arc(G, _, _, _)).

list_graph(G) :-
    graph(G),
    listing(vertex(G, _)),
    listing(arc(G, _, _, _)).

read_graph(G, FileName) :-
    new_graph(G),
    retractall(vertex(G, _)),
    retractall(arc(G, _, _, _)),
    csv_read_file(FileName, List, [separator(0'\t), arity(3)]),
    set_Value(List, Res),
    insert_into_graph(G, Res),
    !.

set_Value([], []) :-
    !.

set_Value([Head | Tail], [row(Vertex1, Vertex2, FinalWeight) | Rest]) :-
    arg(1, Head, Vertex1),
    arg(2, Head, Vertex2),
    arg(3, Head, Weight),
    change(Weight, FinalWeight),
    set_Value(Tail, Rest).

change(Weight, DotNumber) :-
    string_chars(Weight, CharList),
    dot_change(CharList, Ris),
    number_chars(DotNumber, Ris).

dot_change([], []) :-
    !.

dot_change([Head | Tail], [Head | Rest]) :-
    Head \= ',',
    dot_change(Tail, Rest).

dot_change([Head | Tail], ['.' | Rest]) :-
    Head == ',',
    dot_change(Tail, Rest).

insert_into_graph(_, []) :-
    !.

insert_into_graph(G, [Head | Tail]) :-
    functor(Head, row, 3),
    arg(1, Head, Vertex1),
    arg(2, Head, Vertex2),
    arg(3, Head, Weight),
    !,
    new_vertex(G, Vertex1),
    new_vertex(G, Vertex2),
    new_arc(G, Vertex1, Vertex2, Weight),
    insert_into_graph(G, Tail).

write_graph(G, FileName) :-
    write_graph(G, FileName, graph).

write_graph(G, FileName, Type) :-
    graph(G),
    Type = graph,
    !,
    arcs_for_graph(G, Es),
    csv_write_file(FileName, Es, [separator(0'\t)]).


write_graph(G, FileName, Type) :-
    is_list(G),
    check_list(G),
    Type = edges,
    !,
    arcs_for_edges(G, Es),
    csv_write_file(FileName, Es, [separator(0'\t)]).

arcs_for_graph(G, Es) :-
    findall(arc(U, V, Weight), arc(G, U, V, Weight), Es).


check_list([]) :-
    !.

check_list([Head | Tail]) :-
    functor(Head, arc, 4),
    check_list(Tail).

arcs_for_edges([], []) :-
    !.

arcs_for_edges([Head | Tail], [arc(Vertex1, Vertex2, Weight) | Rest]) :-
    arg(2, Head, Vertex1),
    arg(3, Head, Vertex2),
    arg(4, Head, Weight),
    arcs_for_edges(Tail, Rest).


mst_prim(G, Source) :-
    retractall(vertex_key(G, _, _)),
    retractall(vertex_previous(G, _, _)),
    graph(G),
    new_heap(G),
    vertex(G, Source),
    findall(V, vertex(G, V), VertList),
    subtract(VertList, [Source], NewVertList),
    asserta(vertex_key(G, Source, 0)),
    set_to_inf(G, NewVertList),
    prim(G, Source, NewVertList),
    findall(vertex_key(G, Vert, Weight), vertex_key(G, Vert, Weight), List),
    create_previous(List),
    delete_heap(G),
    !.

prim(_, _, [_ | []]) :-
    !.

prim(G, Source, VertList) :-
    vertex_neighbors(G, Source, List_neighbors),
    subtract(VertList, [Source], NewVertList),
    heap_insert_neighbors(G, List_neighbors, NewVertList),
    heap_head(G, WeightKey, Val),
    vertex_key(G, Val, Weight),
    WeightKey < Weight,
    !,
    heap_extract(G, WeightKey, Val),
    retract(vertex_key(G, Val, Weight)),
    assert(vertex_key(G, Val, WeightKey)),
    prim(G, Val, NewVertList).

prim(G, Source, VertList) :-
    heap_head(G, WeightKey, Val),
    heap_extract(G, WeightKey, Val),
    prim(G, Source, VertList),
    !.

prim(G, _, _) :-
    heap_empty(G),
    !.

heap_insert_neighbors(_, [], _) :-
    !.

heap_insert_neighbors(G, [Head | Tail], NewVertList) :-
    arg(3, Head, Val),
    arg(4, Head, Key),
    member(Val, NewVertList),
    !,
    heap_insert(G, Key, Val),
    heap_insert_neighbors(G, Tail, NewVertList).

heap_insert_neighbors(G, [_ | Tail], NewVertList) :-
    heap_insert_neighbors(G, Tail, NewVertList),
    !.

set_to_inf(_, []) :-
    !.

set_to_inf(G, [Head | Tail]) :-
    assert(vertex_key(G, Head, inf)),
    set_to_inf(G, Tail),
    !.

create_previous([]) :-
    !.

create_previous([Head | Tail]) :-
    arg(2, Head, Father),
    arg(1, Head, G),
    vertex_neighbors(G, Father, List_neighbors),
    find_prev(List_neighbors),
    create_previous(Tail),
    !.

find_prev([]) :-
    !.

find_prev([Head | List_neighbors]) :-
    arg(4, Head, Weight),
    arg(3, Head, Son),
    arg(2, Head, Father),
    arg(1, Head, G),
    vertex_key(G, Son, Weight),
    \+ vertex_previous(G, Son, _),
    \+ vertex_previous(G, Father, Son),
    !,
    assert(vertex_previous(G, Son, Father)),
    find_prev(List_neighbors).

find_prev([_ | List_neighbors]) :-
    find_prev(List_neighbors).

mst_get(G, Source, PreorderTree) :-
    vertex_key(G, Source, 0),
    findall(vertex_key(G, V, Source), vertex_previous(G, V, Source), PrevL),
    mst_list(G, Source, PrevL, PreorderTree),
    !.

mst_list(G, Source, PrevL, [arc(G, Source, SmallS, K) | PreorderTree]) :-
    smallest(PrevL, SmallS),
    subtract(PrevL, [vertex_key(_, SmallS, _)], NewPrevL),
    vertex_key(G, SmallS, K),
    findall(vertex_key(G, V, SmallS), vertex_previous(G, V, SmallS), PrevS),
    mst_list(G, SmallS, PrevS, PreorderTreeL),
    mst_list(G, Source, NewPrevL, PreorderTreeR),
    append(PreorderTreeL, PreorderTreeR, PreorderTree),
    !.

mst_list(G, Source, PrevL, [arc(G, Source, SmallS, K) | PreorderTree]) :-
    smallest(PrevL, SmallS),
    subtract(PrevL, [vertex_key(_, SmallS, _)], NewPrevL),
    vertex_key(G, SmallS, K),
    mst_list(G, Source, NewPrevL, PreorderTree),
    !.

mst_list(_, _, [], []) :-
    !.

smallest([Head | []], Result) :-
    arg(2, Head, Son),
    Result = Son,
    !.

smallest([First, Second | ListSon], Result) :-
    arg(2, Second, Son2),
    arg(2, First, Son),
    arg(1, First, G),
    vertex_key(G, Son2, Key2),
    vertex_key(G, Son, KeySon),
    KeySon < Key2,
    smallest([vertex_previous(G, Son, _) | ListSon], Result),
    !.

smallest([First, Second | ListSon], Result) :-
    arg(2, Second, Son2),
    arg(2, First, Son),
    arg(1, First, G),
    vertex_key(G, Son2, Key2),
    vertex_key(G, Son, KeySon),
    KeySon > Key2,
    smallest([vertex_previous(G, Son2, _) | ListSon], Result),
    !.

smallest([First, Second | ListSon], Result) :-
    arg(2, Second, Son2),
    arg(2, First, Son),
    arg(1, First, G),
    vertex_key(G, Son2, Key2),
    vertex_key(G, Son, KeySon),
    KeySon = Key2,
    sort([Son, Son2], [Sorted | _]),
    smallest([vertex_previous(G, Sorted, _) | ListSon], Result),
    !.

new_heap(H) :-
    heap(H, _S),
    !.

new_heap(H) :-
    assert(heap(H, 0)),
    !.

delete_heap(H) :-
    retract(heap(H, _)),
    retractall(heap_entry(H, _, _, _)),
    !.

heap_has_size(H, S) :-
    heap(H, S).

heap_empty(H) :-
    heap_has_size(H, S),
    S = 0.

heap_not_empty(H) :-
    heap(H, S),
    S > 0.

heap_head(H, K, V) :-
    heap(H, _),
    heap_entry(H, 1, K, V),
    !.

heap_insert(H, K, V) :-
    heap(H, Size),
    NewSize is Size + 1,
    retract(heap(H, Size)),
    assert(heap(H, NewSize)),
    assert(heap_entry(H, NewSize, K, V)),
    heapify(H, NewSize),
    !.

heapify(_, Position) :-
    Position == 1,
    !.

heapify(_, Position) :-
    Position == 0,
    !.

heapify(H, Position) :-
    Parent is div(Position, 2),
    heap_entry(H, Parent, KParent, _),
    heap_entry(H, Position, KChildren, _),
    KParent =< KChildren,
    !.

heapify(H, Position) :-
    Parent is div(Position, 2),
    heap_entry(H, Parent, KParent, Val1),
    heap_entry(H, Position, KChildren, Val2),
    KChildren =< KParent,
    retract(heap_entry(H, Parent, KParent, _)),
    retract(heap_entry(H, Position, KChildren, _)),
    assert(heap_entry(H, Parent, KChildren, Val2)),
    assert(heap_entry(H, Position, KParent, Val1)),
    heapify(H, Parent),
    !.

heap_extract(H, K, V) :-
    heap_entry(H, _, K, V),
    heap_has_size(H, Size),
    NewSize is Size - 1,
    NewSize == 0,
    !,
    retract(heap(H, Size)),
    assert(heap(H, NewSize)),
    retract(heap_entry(H, 1, K, V)).

heap_extract(H, K, V) :-
    heap_entry(H, Position, K, V),
    heap_has_size(H, Size),
    NewSize is Size - 1,
    CheckLeft is Position * 2,
    CheckRight is (Position * 2) + 1,
    CheckLeft > Size,
    CheckRight > Size,
    retract(heap(H, Size)),
    assert(heap(H, NewSize)),
    retract(heap_entry(H, Position, K, V)),
    retract(heap_entry(H, Size, LastKey, LastVal)),
    assert(heap_entry(H, Position, LastKey, LastVal)),
    heapify(H, Position),
    !.

heap_extract(H, K, V) :-
    heap_entry(H, Position, K, V),
    heap_has_size(H, Size),
    NewSize is Size - 1,
    retract(heap(H, Size)),
    assert(heap(H, NewSize)),
    retract(heap_entry(H, Position, K, V)),
    retract(heap_entry(H, Size, LastKey, LastVal)),
    assert(heap_entry(H, Position, LastKey, LastVal)),
    heapify_extract(H, Position, NewSize),
    !.

heapify_extract(_, Position, Size) :-
    RightSon is (Position * 2) + 1,
    LeftSon is Position * 2,
    RightSon > Size,
    LeftSon > Size,
    !.

heapify_extract(H, Position, Size) :-
    LeftSon is Position * 2,
    RightSon is (Position * 2) + 1,
    LeftSon =< Size,
    RightSon =< Size,
    heap_entry(H, Position, FatherKey, _),
    heap_entry(H, LeftSon, LeftKey, _),
    heap_entry(H, RightSon, RightKey, _),
    LeftKey < FatherKey,
    LeftKey =< RightKey,
    swap(H, LeftSon, Position),
    heapify_extract(H, LeftSon, Size),
    !.

heapify_extract(H, Position, Size) :-
    LeftSon is Position * 2,
    RightSon is (Position * 2) + 1,
    LeftSon =< Size,
    RightSon =< Size,
    heap_entry(H, Position, FatherKey, _),
    heap_entry(H, LeftSon, LeftKey, _),
    heap_entry(H, RightSon, RightKey, _),
    RightKey < FatherKey,
    RightKey =< LeftKey,
    swap(H, RightSon, Position),
    heapify_extract(H, RightSon, Size),
    !.

heapify_extract(H, Position, Size) :-
    LeftSon is Position * 2,
    RightSon is (Position * 2) + 1,
    LeftSon =< Size,
    RightSon =< Size,
    heap_entry(H, Position, FatherKey, _),
    heap_entry(H, LeftSon, LeftKey, _),
    heap_entry(H, RightSon, RightKey, _),
    FatherKey =< LeftKey,
    FatherKey =< RightKey,
    !.

heapify_extract(H, Position, Size) :-
    LeftSon is Position * 2,
    RightSon is (Position * 2) + 1,
    LeftSon == Size,
    RightSon > Size,
    heap_entry(H, Position, FatherKey, _),
    heap_entry(H, LeftSon, LeftKey, _),
    LeftKey < FatherKey,
    swap(H, Position, LeftSon),
    !.

heapify_extract(H, Position, Size) :-
    LeftSon is Position * 2,
    RightSon is (Position * 2) + 1,
    LeftSon == Size,
    RightSon > Size,
    heap_entry(H, Position, FatherKey, _),
    heap_entry(H, LeftSon, LeftKey, _),
    FatherKey =< LeftKey,
    !.


swap(H, SonPosition, Position) :-
    retract(heap_entry(H, SonPosition, SonKey, SonVal)),
    retract(heap_entry(H, Position, FatherKey, FatherVal)),
    assert(heap_entry(H, Position, SonKey, SonVal)),
    assert(heap_entry(H, SonPosition, FatherKey, FatherVal)).

list_heap(H) :-
    heap(H, _),
    listing(heap(H, _)),
    listing(heap_entry(H, _, _, _)).

%%% end of file mst
