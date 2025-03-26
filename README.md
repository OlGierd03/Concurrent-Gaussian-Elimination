# Concurrent-Gaussian-Elimination

## Overview

This repository contains an implementation of a low-level concurrent version of the
Gaussian elimination algorithm in **Java**. The program generates the **Diekert graph**
and computes the **Foata normal form**, which is subsequently used to design the order of concurrent operations by analyzing dependencies and indivisible steps.

## Część teoretyczna

Dla zadanego układu równań:

$$
\begin{bmatrix}
M_{1,1} & M_{1,2} & \cdots & M_{1,m} \\
M_{2,1} & M_{2,2} & \cdots & M_{2,m} \\
\vdots & \vdots & \ddots & \vdots \\
M_{n,1} & M_{n,2} & \cdots & M_{n,m}
\end{bmatrix}
\begin{bmatrix}
x_{1} \\
x_{2} \\
\vdots \\
x_{m}
\end{bmatrix}
=
\begin{bmatrix}
y_1 \\
y_2 \\
\vdots \\
y_n
\end{bmatrix}
$$

algorytm Gaussa wykonujemy dla macierzy przedstawionej jako:

$$
\begin{bmatrix}
M_{1,1} & M_{1,2} & \cdots & M_{1,m} & | & y_1 \\
M_{2,1} & M_{2,2} & \cdots & M_{2,m} & | & y_2 \\
\vdots & \vdots & \ddots & \vdots & | & \vdots \\
M_{n,1} & M_{n,2} & \cdots & M_{n,m} & | & y_n
\end{bmatrix}
$$

### Podstawowe niepodzielne zadania obliczeniowe

Algorytm Gaussa należy podzielić na następujące niepodzielne operacje:
- $A_{i, k}$ - znalezienie następującego mnożnika wiersza i-tego w celu odjęcia iloczynu od wiersza k-tego: $m_{i, k} := \frac{M_{k, i}}{M_{i, i}}$,
- $B_{i, j, k}$ - pojedyncze pomnożenie j-tego elementu wiersza i-tego korzystając ze znalezionego mnożnika w $A_{i, k}$: $n_{i, j, k} = m_{i, k} \cdot M_{i, j}$,
- $C_{i, j, k}$ - odjęcie j-tego elementu i-tego wiersza, pomnożonego przez mnożnik, od wiersza k: $M_{k,j} := n_{i,j,k}$.

### Alfabet w sensie teorii śladów

W oparciu o opisane niepodzielne zadania obliczeniowe możemy sformułować alfabet postaci:

$$
\Sigma = \left\{ A_{i, k}, B_{i, j, k}, C_{i, j, k} \right\}
$$

gdzie:

- $i \in \{ 1, 2, \ldots, n-1 \}$
- $j \in \{ i, i+1, \ldots, m+1 \}$
- $k \in \{ i+1, i+2, \ldots, n \}$

### Ciąg zadań obliczeniowych wykonywanych przez algorytm sekwencyjny

Algorytm Gaussa wykonywany sekwencyjnie zakłada korzystanie z i-tego wiersza do zerowania wszystkich poniższych k-tych wierszy, co iterację korzystając z kolejnego i-wiersza. I tak np. zerując wiersz $k=2$ z pomocą wiersza $i=1$, ciąg zadań wykonywanych sekwencyjnie jest następujący:

$$
A_{1,2}, B_{1,1,2}, C_{1,1,2}, B_{1,2,2}, C_{1,2,2}, \ldots, B_{1,m+1,2}, C_{1,m+1,2}
$$

Z powyższego nie trudno zapisać uogólnienie podsumowanie, że szukany ciąg zadań jest:

$$
p_{1, 2}, p_{1, 3}, \ldots, p_{1, n}, p_{2, 3}, p_{2, 4}, \ldots, p_{2, n}, \ldots, p_{n-1, n}
$$

gdzie $p$ to podciągi postaci:

$$
p_{i, k} = \left( A_{i,k}, B_{i,i,k}, C_{i,i+1,k}, B_{i,i+2,k}, C_{i,i+2,k}, \ldots, B_{i,m+1,k}, C_{i,m+1,k} \right)
$$

### Relacje (nie)zależności

Wyznaczono relacje zależności:

$$
D = \text{sym}\left(\left\{ (A_{i,k}, B_{i,j,k}), (B_{i,j,k}, C_{i,j,k}), (C_{i-1,j,k}, C_{i,j,k}), (C_{i-1,j,k-1}, B_{i,j,k}), (C_{i-1,i,k}, A_{i,k}), (C_{i-1,i,i}, A_{i,k}) \right\} \right)
$$

gdzie:

- $i \in \{ 1, 2, \ldots, n-1 \}$
- $j \in \{ i, i+1, \ldots, m+1 \}$
- $k \in \{ i+1, i+2, \ldots, n \}$

Relacje niezależności można wyznaczyć jako:

$$
I = \Sigma^2 - D
$$

### Graf zależności Diekerta

Graf Diekerta wyznaczany jest wykorzystując program napisany w języku Java (do wizualizacji skorzystano z graphviz).

Program realizuje kolejno następujące kroki:
1. Stworzenie wierzchołków grafu; każdy element alfabetu (podrozdział 2.2) stanowi wierzchołek,
2. Dodanie krawędzi skierowanych odpowiadających odpowiednim relacjom zależności (podrozdział 2.4),
3. Usunięcie zbędnych krawędzi.