#!/bin/bash

function create_directories {
    mkdir -p ./data/.config/
    cd ./data/.config/
}

function create_files {
    touch secrets.properties
    echo "clientId=\"client_id\"" >> secrets.properties
    echo "clientSecret=\"client_secret\"" >> secrets.properties
    echo "tmdbSecret=\"tmdb_secret\""
}


create_directories
create_files

