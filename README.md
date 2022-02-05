# An attempt at a Hateoas setup for quarkus

Future todo:

- Recursively append links to @HalResponse's
- Automatically add `_self`

## Material

- https://restfulapi.net/hateoas/
- https://tools.ietf.org/id/draft-kelly-json-hal-01.html

## Main goals

- Create an easily usable API to create HATEOAS HAL geneeration on JAX-RS controllers
- Allow usage in classes and records
- Integrate well with Quarkus setups
- Allow customization of object and route object names

Current progress:

```http
GET http://localhost:8080/posts

HTTP/1.1 200 OK
Content-Type: application/json
content-length: 1279

[
  {
    "id": 1,
    "title": "Post one",
    "content": "Lorem Ipsum",
    "comments": [
      {
        "id": 1,
        "content": "Great video!",
        "sender": 1
      }
    ],
    "links": {
      "add": {
        "href": "posts",
        "rel": "add",
        "type": "POST"
      },
      "list_posts": {
        "href": "posts",
        "rel": "list_posts",
        "type": "GET"
      },
      "update": {
        "href": "posts",
        "rel": "update",
        "type": "PATCH"
      },
      "byId": {
        "href": "posts/1",
        "rel": "byId",
        "type": "GET"
      },
      "delete": {
        "href": "posts/1",
        "rel": "delete",
        "type": "DELETE"
      }
    }
  },
  {
    "id": 2,
    "title": "Post two",
    "content": "Foo bar",
    "comments": [
      {
        "id": 7,
        "content": "Great video!",
        "sender": 1
      },
      {
        "id": 5,
        "content": "Great video! !!!",
        "sender": 2
      },
      {
        "id": 6,
        "content": "Great video dude!",
        "sender": 6
      }
    ],
    "links": {
      "add": {
        "href": "posts",
        "rel": "add",
        "type": "POST"
      },
      "list_posts": {
        "href": "posts",
        "rel": "list_posts",
        "type": "GET"
      },
      "update": {
        "href": "posts",
        "rel": "update",
        "type": "PATCH"
      },
      "byId": {
        "href": "posts/2",
        "rel": "byId",
        "type": "GET"
      },
      "delete": {
        "href": "posts/2",
        "rel": "delete",
        "type": "DELETE"
      }
    }
  },
  {
    "id": 3,
    "title": "Post one",
    "content": "Bazzzz",
    "comments": [
      {
        "id": 2,
        "content": "FOOBAR",
        "sender": 1
      }
    ],
    "links": {
      "add": {
        "href": "posts",
        "rel": "add",
        "type": "POST"
      },
      "list_posts": {
        "href": "posts",
        "rel": "list_posts",
        "type": "GET"
      },
      "update": {
        "href": "posts",
        "rel": "update",
        "type": "PATCH"
      },
      "byId": {
        "href": "posts/3",
        "rel": "byId",
        "type": "GET"
      },
      "delete": {
        "href": "posts/3",
        "rel": "delete",
        "type": "DELETE"
      }
    }
  }
]

```
