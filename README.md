# Blast GUI

<p align="center"><img width="100" src="./assets/dna.png" alt="Blast GUI logo"></p>

<p align="center">
<img alt="GitHub top language" src="https://img.shields.io/github/languages/top/olegbrz/Blast-GUI">
<img alt="GitHub code size in bytes" src="https://img.shields.io/github/languages/code-size/olegbrz/Blast-GUI">
<img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/olegbrz/Blast-GUI">
</p>

![screenshot](assets/screenshot.png)

## Project description

Blast is a program to search sequence matches in protein and nucleotid sequences. The goal of
this project was build a GUI for the provided version, which has a public method that allows to
make the queries. The query parameters are:
- The query type. The query can be for proteins or for nucleotid sequences.
- The data base file. The file containing the proteins or the nucleotid sequences where to
search for the matches.
- The data base indexes file. The file containing the indexes where the proteins begin in the
data base file.
- The similarity percentage. A float value between 0.0 and 1.0.
- The query sequence. A string that represents the protein or nucleotid sequence for which
the matches are search. Every aminoacid or nucleotid is represented by an uppercase letter.

## Problems

- [x] `JRadioButton`s should let select only one option (`JRadioButton`s were added to a `ButtonGroup`)
- [x] Need to get path of selected files (`JFileChooser` was used)
    - [x] Need to filer allowed `.index` files (`FileNameExtensionFilter` was used)
- [x] Needed to use `ActionLister`s for file choosing, and query button
- [x] Show results inside the window and not in the `System.out`. (`JScrollPane` and `JTextArea` were used to display the results)
    - [x] `JTextArea` overflown horizontally due to results columns, the solution was `.setLineWrap(true);`
- [x] Add icon to `JFrame`. (Used `.setIconImage()`)
- [x] Side menu's layout (`GridLayout`) is bad as the `JPanel` item stretch with window. Maybe the solution is `BoxLayout`.
- [x] The code was too long to be declared in only one class, so it was necessary a major refactoring.
- [x] ``JSlider`` does not accept float values, so it was needed to change the labels manually and scale the slider from 100 to 1.