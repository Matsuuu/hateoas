# An attempt at a Hateoas setup for quarkus

Future todo:

- Allow naming relations via annotations
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
content-length: 1135

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
    "links": [
      {
        "href": "posts",
        "rel": "add",
        "type": "POST"
      },
      {
        "href": "posts",
        "rel": "update",
        "type": "PATCH"
      },
      {
        "href": "posts/1",
        "rel": "delete",
        "type": "DELETE"
      },
      {
        "href": "posts",
        "rel": "getAll",
        "type": "GET"
      },
      {
        "href": "posts/1",
        "rel": "byId",
        "type": "GET"
      }
    ]
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
    "links": [
      {
        "href": "posts",
        "rel": "add",
        "type": "POST"
      },
      {
        "href": "posts",
        "rel": "update",
        "type": "PATCH"
      },
      {
        "href": "posts/2",
        "rel": "delete",
        "type": "DELETE"
      },
      {
        "href": "posts",
        "rel": "getAll",
        "type": "GET"
      },
      {
        "href": "posts/2",
        "rel": "byId",
        "type": "GET"
      }
    ]
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
    "links": [
      {
        "href": "posts",
        "rel": "add",
        "type": "POST"
      },
      {
        "href": "posts",
        "rel": "update",
        "type": "PATCH"
      },
      {
        "href": "posts/3",
        "rel": "delete",
        "type": "DELETE"
      },
      {
        "href": "posts",
        "rel": "getAll",
        "type": "GET"
      },
      {
        "href": "posts/3",
        "rel": "byId",
        "type": "GET"
      }
    ]
  }
]
```
