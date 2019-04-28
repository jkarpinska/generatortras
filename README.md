# GeneratorTras
Desktop app that generates a list of routes from an initial list based on given parameteres (expected kilometers sum or number of routes to draw).

## Motivation
Craeted upon request for practical use and to train importing data from external files and exporting to external files (including excel spreadsheets), creating an app with mutliple windows.

## Functionality
- Option to select an initial list of routes from existing file, last used file or to create a new initial list.
- Initial list can be edited (add/remove/edit individual routes) and saved to a file.
- Based on given parameters (minimum sum of routes lengths, number of routes to draw) and the initial list the app is generating a new pseudorandom list of routes. 
- The generated list can be saved to a new excel spreadsheet in a chosen directory.

## Technology
- Java SE 8 + JavaFX
- Libraries: Apache POI 4.0

## Screenshots
![image](https://user-images.githubusercontent.com/49813577/56867806-47d64800-69ea-11e9-80c6-7eb643d17f87.png)

![image](https://user-images.githubusercontent.com/49813577/56867813-62102600-69ea-11e9-9f68-7a2d00e13b6e.png)

![image](https://user-images.githubusercontent.com/49813577/56867822-8e2ba700-69ea-11e9-9890-219c461ed2d6.png)

## Build status
Basic functionality finished.

## Planned improvements
- Fix issues with handling errors and error pop-up messages.
- Enhace generating process to consider maximum kilometers parameter as well.
- Add preferences file
- Build .exe file for istallation purposes.

## Credits
App icon from [Ontimedia](https://www.iconfinder.com/Ontimedia)
