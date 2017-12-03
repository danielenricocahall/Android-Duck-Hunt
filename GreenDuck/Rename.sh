#!/bin/bash

for filename in .; do 
    [ -f "$filename" ] || continue
    mv "$filename" "${filename//[www.imagesplitter.net]-/}"

done
