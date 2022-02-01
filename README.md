# An attempt at a Hateoas setup for quarkus

Future todo:

- Create an annotation to mark the "links" property in a class
- Allow mapping of id's in paths
- Safer operations
- Allow naming relations via annotations
- Consider mapping links as a HashMap instead of an list

Current progress:

```
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
