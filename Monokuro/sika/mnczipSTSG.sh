echo "converting to 50% ...";
for i in ./STSG/*.png 
do
    if test -f "$i" 
    then
       echo "Doing somthing to $i"
       convert $i -resize 50% $i 
    fi
done

java -jar ../dist/Monokuro.jar -d STSG STSG

echo "all to 2-bit png..."

java -jar ../dist/Monokuro.jar -d! STSG STSGMNC

echo "done"

#cd /Users/Sinri/Codes/Monochrome/Monokuro/dist/
echo "begin"

cd STSGMNC
for i in *.mnc
do
    if test -f "$i" 
    then
       echo "zip $i"
       zip ../zipped/STSG/"$i".zip $i
    fi
done