# NPC Data Manager Storage
Spring boot application to provide access and management of S3 objects.

Allows to manage directory and its resources. The objects are managed by means of
directories and resources. Directories are holders of resources and resources are the
objects (images, docs, etc) within the directories.


## Directory
The following functionalities are available for directory management:

- Create a new directory:
  - Request: POST ``http://localhost/storage/directory?name=dir_name``
  - Response: HTTP 200 - Response content: directory_id (Long)
- Delete a directory:
    - Request: DELETE ``http://localhost/storage/directory/directory_id``
    - Response: HTTP 200
- List all directories:
  - Request: GET ``http://localhost/storage/directory/all``
  - Response: HTTP 200 - JSON response content: [ "dir_name_a", "dir_name_b, "dir_name_c" ]
- Lists resources within the directory:
  - Request: GET ``http://localhost/storage/directory/{id}/list``
  - Response: HTTP 200 - JSON response content: [ { "id": 1, "name": "resA", "type": "IMAGE", "storageId": "9961d87a314b44a29ec3fe91c019126f" }, ... ]

## Resource
The following functionalities are available for resource management:

- Create a new resource:
  - Request: POST ``http://localhost/storage/resource/upload``
     - Request form-data:
        - file=multipart-file;
        - name=resource name;
        - type=resource type;
        - directoryId=target directory. 
  - Response: HTTP 200 - Response content: directory_id (Long)
- Delete a resource:
  - Request: DELETE ``http://localhost/storage/resource/{id}``
  - Response: HTTP 200
- Delete many resources at once:
  - Request: DELETE ``http://localhost/storage/resource``
    - JSON Request body: [ 2, 3, 4, 5, ...] (resources ids).
  - Response: HTTP 200
