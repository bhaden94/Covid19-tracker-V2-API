# Covid19-tracker-V2-API

<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
<!-- [![Contributors][contributors-shield]][contributors-url] -->
![code-quality-shield]
![code-grade]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]



<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://covid-tracker-v2.herokuapp.com/">
    <img src="https://images.newscientist.com/wp-content/uploads/2020/02/11165812/c0481846-wuhan_novel_coronavirus_illustration-spl.jpg" alt="Logo" width="150" height="100">
  </a>

  <h3 align="center">Covid Tracker V2 Frontend</h3>

  <p align="center">
    Visualization dashboard to see key data related to Covid-19.
    <br />
    <a href="https://covid-tracker-v2.herokuapp.com/">View On Heroku</a>
    ·
    <a href="https://github.com/bhaden94/Covid19-tracker-V2-API/issues">Report Bug</a>
    ·
    <a href="https://github.com/bhaden94/Covid19-tracker-V2-API/issues">Request Feature</a>
  </p>
</p>



<!-- TABLE OF CONTENTS -->
## Table of Contents

* [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [Usage](#usage)
* [Roadmap](#roadmap)
* [Contributing](#contributing)
* [License](#license)
* [Contact](#contact)
* [Acknowledgements](#acknowledgements)



<!-- ABOUT THE PROJECT -->
## Built With
* [Spring Initializer](https://start.spring.io/)
* [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)


<!-- GETTING STARTED -->
## Getting Started

To install the Spring Boot backend follow the below steps. After this is successfully installed, then you may install the React frontend by going [here](https://github.com/bhaden94/covid-19-tracker-v2-fe).

### Prerequisites

* Java 11 JDK or higher must be installed.
* MongoDB must be installed and running with a database named `covid_db`.

### Installation

1. Clone the repo
```shell script
git clone https://github.com/bhaden94/Covid19-tracker-V2-API.git
```
2. Run in development mode
```shell script
gradlew bootRun --args='--spring.profiles.active=dev'
```
3. Running with IntelliJ
    * Go to src/main/resources/application.properties
    * Change spring.profiles.active to `dev`
    * You can now start with IntelliJ


<!-- USAGE EXAMPLES -->
## Usage

This is the API and services of the Covid-19 Tracker. Upon startup, the services will pull data from 
[Johns Hopkins University Center for Systems Science and Engineering (JHU CSSE)](https://github.com/CSSEGISandData/COVID-19)
and insert it into your local running MongoDB.

<!-- ROADMAP -->
## Roadmap

See the [open issues](https://github.com/bhaden94/Covid19-tracker-V2-API/issues) for a list of proposed features (and known issues).


<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to be; learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.



<!-- CONTACT -->
## Contact

Brady Haden - [LinkedIn](https://www.linkedin.com/in/brady-s-haden/)




<!-- ACKNOWLEDGEMENTS -->
## Acknowledgements
* [Johns Hopkins University Center for Systems Science and Engineering (JHU CSSE)](https://github.com/CSSEGISandData/COVID-19)
* [Img Shields](https://shields.io)
* [Code Inspector Github Action](https://github.com/marketplace/actions/code-inspector-github-action)





<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
<!-- [contributors-shield]: https://img.shields.io/github/contributors/othneildrew/Best-README-Template.svg?style=flat-square
[contributors-url]: https://github.com/othneildrew/Best-README-Template/graphs/contributors -->
[forks-shield]: https://img.shields.io/github/forks/bhaden94/Covid19-tracker-V2-API.svg
[forks-url]: https://github.com/bhaden94/Covid19-tracker-V2-API/network/members

[stars-shield]: https://img.shields.io/github/stars/bhaden94/Covid19-tracker-V2-API.svg
[stars-url]: https://github.com/bhaden94/Covid19-tracker-V2-API/stargazers

[issues-shield]: https://img.shields.io/github/issues/bhaden94/Covid19-tracker-V2-API.svg
[issues-url]: https://github.com/bhaden94/Covid19-tracker-V2-API/issues

[license-shield]: https://img.shields.io/github/license/bhaden94/Covid19-tracker-V2-API.svg
[license-url]: https://github.com/bhaden94/Covid19-tracker-V2-API/blob/master/LICENSE.txt

[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/brady-s-haden/
[product-screenshot]: images/screenshot.png

[code-quality-shield]: https://www.code-inspector.com/project/16564/score/svg
[code-grade]: https://www.code-inspector.com/project/16564/status/svg

