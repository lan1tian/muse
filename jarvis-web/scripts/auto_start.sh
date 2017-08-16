cd ../projects;
rm -rf jarvis;
mkdir jarvis;
cd ./jarvis;
jar -xvf ../jarvis.war;
cd ../../bin;
sh startup.sh