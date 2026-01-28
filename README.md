# Dragons of Mugloar
Author: Marek Lepik

## Project overview

This repository contains a bot designed to play the game "Dragons of Mugloar". The backend is implemented using Java and Spring Boot to align with the enterprise's core technology stack.
Frontend is built with Vue 3 and Vite, providing a modern and efficient user interface.

**Project structure**  
The repository contains two independent modules:

- `backend/` – Spring Boot REST API responsible for game logic and automated gameplay

- `frontend/` – Vue.js single-page application for user interaction

This mirrors a typical enterprise setup where frontend and backend are developed and deployed independently.

State management is implemented using Vue 3’s Composition API with a centralized reactive store. A lightweight approach was chosen instead of Pinia to avoid unnecessary build tooling for a small application.”

# Backend
The backend is built with Java and Spring Boot, providing both a RESTful API for game logic
and automated gameplay and CLI for easy startup.
It handles game state management, decision-making algorithms.
Bot is built to have score limit so the requirement is fulfilled and API is not abused.

## Starting the backend

To start the backend server, navigate to the `backend/` directory and run the following command:
The backend includes unit test for decision-making logic.

Steps:

#### 1. Navigate to backend directory
``cd backend``
#### 2. Build the project
``./mvnw clean package``
#### 3. Run the application
this will start the bot playing automatically with score limit of 1000:

``java -jar target/mugloar.jar start``

Score limit can be passed also via argument

``java -jar target/mugloar.jar start 1500``

This will start REST API only

``java -jar target/mugloar.jar``

#### 4. Run tests
``./mvnw test``

Or using Maven directly:
#### 3. Run the application
``./mvnw spring-boot:run``

### The backend server will start on `http://localhost:8080`.

Backend also will have webapi from where bot can be started:

``http://localhost:8080/api/bot/start?scoreLimit=100``

# Bot logic and high score

# General bot playing loop logic
Description of the general bot playing loop
## Loop
After initialization, the bot enters a loop where it continuously reads the game state, makes decisions, and outputs actions until the game ends.
1. Fetches Task list
2. Fetches Shop list
3. Updates bot state with current game state: Lives and gold from last turn decision
4. Enter decision service
   - Decides which task to choose
   - Decides which items to buy from the shop
5. Based on previous both decisions:
   - Checks if user needs healing (prioritizes)
   - Checks selected task probability
   - If task simple enough, executes it before buying items
   - Otherwise, tries to buy item - restarts the loop since buying an item may change the task decision (chances)
   - Fallback to task if no items can be bought
6. Returns decision to game engine
7. Action is made
8. Repeat until game ends


## High score
While developing the bot performance test was made:

Best score achieved (achieving 2nd place in the leaderboard):

- End of turn 9151
- Gold = 85531068
- Lives = 4
- Score = 87,262,068
- Game ID: p7VFBsGs

# Frontend

Frontend is built with Vue 3 and Vite, providing a modern and efficient user interface for user interaction.
It uses minimal dependencies to keep the build lightweight and fast.
The build is also hostable without nodejs server, making it easy to deploy in various environments.
Project is using Tailwind CSS for rapid UI development and styling.

The frontend is accessable via Github pages:

[Link to github pages](https://mareklepik.github.io/mugloar/)


## Overview

The project has:
- Shared api client for communication with backend
- Simple state management
- Toast notifications for user feedback
- Basic responsive design for usability on different devices
- Simple and clean UI for easy navigation

## Project startup

#### 1. Navigate to frontend directory
``cd frontend``

#### 2. Install dependencies
``npm install``

#### 3. Start development server
``npm run dev``

The frontend server will start on `http://localhost:5173/mugloar/`.

## Build for production

To build the frontend for production, run the following command:
``npm run build``

This will create a `dist/` directory with the production build of the application.

## Preview production build

To preview the production build locally, run the following command:
``npm run preview``

This will start a local server to serve the contents of the `dist/` directory.
`http://localhost:4173/mugloar/`