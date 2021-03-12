# Blast GUI

## Problems

- [x] `JRadioButton`s should let select only one option (`JRadioButton`s were added to a `ButtonGroup`)
- [x] Need to get path of selected files (`JFileChooser` was used)
    - [x] Need to filer allowed `.index` files (`FileNameExtensionFilter` was used)
- [x] Needed to use `ActionLister`s for file choosing, and query button
- [x] Show results inside the window and not in the `System.out`. (`JScrollPane` and `JTextArea` were used to display the results)
    - [x] `JTextArea` overflown horizontally due to results columns, the solution was `.setLineWrap(true);`
- [x] Add icon tu `JFrame`. (Used `.setIconImage()`)
- [ ] Side menu's layout (`GridLayout`) is bad as the `JPanel` item stretch with window. Maybe the solution is `BoxLayout`.