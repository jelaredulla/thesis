function [caughtData, escapedData] = runHCGame(baseV, baseR, gammaVal, betaVal, ...
    maxT, figNum, arenaNum)
    
    cd('hcMATLABSimPlots');
    folderName = sprintf('hc_%.3f_%.3f', gammaVal, betaVal);
    mkdir(folderName);
    cd(folderName);
    
    figure(figNum);
    hold on;
%     caught = plot(nan, nan, 'r*');
%     escaped = plot(nan, nan, 'g*');
%     fd = open('data.txt');
    captureL = betaVal*baseR;

    dR = captureL;
    dTheta = pi/8;
    
    caughtData = [];
    escapedData = [];
    
    
    rInit = 2*captureL;
    while (rInit <= 11*captureL)
        thetaInit = 0;
        while (thetaInit <= pi)
            [t, c] = HCForwardTimeGlobal(baseV, baseR, gammaVal, ...
                betaVal, maxT, rInit, thetaInit, arenaNum);
                       
            xInit = rInit*cos(thetaInit);
            yInit = rInit*sin(thetaInit);
            
            figure(figNum);
            hold on;
            if (c)
                caughtData = [caughtData; [xInit, yInit]];
            else
                escapedData = [escapedData; [xInit, yInit]];
            end
          
            thetaInit = thetaInit + dTheta;
        end
        rInit = rInit + dR;
        
    end
           
    cd ..
    cd ..
end