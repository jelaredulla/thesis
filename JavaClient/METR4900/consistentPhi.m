function result = consistentPhi(xt, yt, phi)
    if (yt == 0)
        if (xt < 0)
            desiredPhi = 1;
        else
            desiredPhi = 0;
        end
    else
        desiredPhi = sign(yt);
    end
        
    if (desiredPhi == phi)
        result = 1;
    else
        result = 0;
    end
end