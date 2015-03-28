for i in ./XB353/*.png 
do
    if test -f "$i" 
    then
       echo "Doing somthing to $i"
       convert $i -resize 50% $i 
    fi
done

for i in ./XBFL/*.png
do
    if test -f "$i"
    then
       echo "Doing somthing to $i"
       convert $i -resize 50% $i        
    fi
done

for i in ./XBII/*.png
do
    if test -f "$i"
    then
       echo "Doing somthing to $i"
       convert $i -resize 50% $i        
    fi
done

for i in ./DJGC/*.png
do
    if test -f "$i"
    then
       echo "Doing somthing to $i"
       convert $i -resize 50% $i        
    fi
done
