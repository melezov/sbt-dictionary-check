cd ..
call sbt "project core" clean publishLocal

cd example
call sbt clean compile
