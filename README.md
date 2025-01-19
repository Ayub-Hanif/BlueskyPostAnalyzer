# ECS160 HW1: Social Media Analysis

[![Build](https://github.com/Ayub-Hanif/BlueskyPostAnalyzer/actions/workflows/maven.yml/badge.svg)](https://github.com/Ayub-Hanif/BlueskyPostAnalyzer/actions)

A Java application to store and analyze social media posts from Bluesky.
- Parses `input.json`
- Stores data in PostgreSQL
- Calculates statistics (total posts, avg replies, etc.)
- Supports weighted analysis

## Getting Started
1. Clone this repo.
2. Run `mvn clean install` in IntelliJ or terminal.
3. Run the app with:
   ```bash
   java -jar target/HW1-solution-1.0-SNAPSHOT.jar
## Database Setup
1. **Install PostgreSQL** (if you haven’t already).
2. **Create a database**. Example:
   ```bash
   createdb socialmedia_db