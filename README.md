# Social Seed - Build Your Own Social Network with Spring Boot, Neo4j, and Vue.js

Welcome to Social Seed, a versatile social network seed project built with Spring Boot, Neo4j, and Vue.js. This project serves as a foundation that allows anyone to cultivate and customize their unique social network.

## Project Overview

Social Seed provides a solid starting point for creating a personalized social network using the powerful combination of Spring Boot for the backend, Neo4j as the graph database, and Vue.js for the frontend. Whether you're building a community for a specific interest, a niche group, or just your friends, this seed project empowers you to shape and expand your social platform.

## Key Features

- **Flexible Architecture:** Built with Spring Boot, Neo4j, and Vue.js, the project offers a flexible and modern stack for customization.
- **Community Building:** Create and nurture your community tailored to your specific interests or audience.
- **Scalable:** Easily scale your social network as your community grows.
- **Open-Source:** Being open-source, it encourages collaboration and contributions.

## DevBlog #1
https://github.com/daironpf/SocialSeed/assets/45364687/fb688225-99f0-44b9-9fc3-7d604845385f

## This is the Graph Data Model used for the project
![This is the Graph Data Model used for the project](https://github.com/daironpf/SocialSeed/blob/main/Neo4j/Graph%20Data%20Model/SocialSeed%20(SS).png)

<!--
## Getting Started

1. Clone the repository: `git clone git@github.com:daironpf/SocialSeed.git`
2. Set up your Neo4j database and update properties in `application.properties`.
3. Navigate to the `frontend` directory and install Vue.js dependencies: `cd frontend && npm install`
4. Run the Vue.js application: `npm run serve`
5. Run the Spring Boot application: `./mvnw spring-boot:run`
-->

## Customization

This project is designed to be easily customizable. Personalize it by adding features, modifying the user interface, or integrating additional technologies to meet your unique requirements.

## Contributions

Contributions are encouraged! Whether it's adding new features, fixing bugs, or enhancing documentation, your contributions help make Social Seed better for everyone.

## Contact

- **Developer:** Dairon Pérez Frías
- **Email:** dairon.perezfrias@gmail.com

Thanks for choosing Social Seed as the starting point for your social network journey! We look forward to seeing the incredible Social Network that you will build.

---

**Note:** This README will be updated as the project progresses, currently everything I want to achieve is reflected here and where I am going can be seen in the code.


Si quieres levantar el proyecto con los datos de prueba generados por el fake graph entonces ejecuta este comando:
in linux o gitbash:
GENERATE_FAKE_GRAPH=true docker-compose up --build fake-graph

in windows terminal:
$env:GENERATE_FAKE_GRAPH="true" ; docker-compose up --build fake-graph

---

I extend my sincere gratitude to JetBrains for generously providing me with their licenses for open source projects to work with their software, which has allowed for faster and more comfortable development.

![JetBrains logo support](https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg)
