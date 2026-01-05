# 🎬 Movie API

## Overview
Movie API is a RESTful service for managing **Movies**, **Genres**, and **Actors**.  


---

## Quick Start

**Requirements:**  
- Java 17+  
- Maven 3.9+  

**Clone and run the project:**
```bash
git clone https://gitea.kood.tech/johanneskristiankonks/movie-api.git
cd movie-api
mvn spring-boot:run
```

The API will run on:  
```
http://localhost:8080
```

---

## Using Postman

You can import the included Postman collection to test the API:

1. Open **Postman**.  
2. Click **File → Import → Upload Files**.  
3. Navigate to `src/main/resources` and select the JSON collection.  
   - Or, copy the JSON content and paste it into **Import → Raw Text**.  
4. The collection will appear in Postman with preconfigured requests.

The database already includes sample **Movies**, **Genres**, and **Actors**, so you can immediately start sending GET requests.

---

## Request Examples

**Create Genre**
```json
{
  "name": "Action"
}
```

**Create Actor**
```json
{
  "name": "Tom Hanks",
  "birthDate": "1956-07-09"
}
```

**Create Movie (use existing IDs)**
```json
{
  "title": "Inception",
  "releaseYear": 2010,
  "duration": 148,
  "genres": [{ "id": 1 }, { "id": 4 }],
  "actors": [{ "id": 3 }, { "id": 14 }]
}
```
