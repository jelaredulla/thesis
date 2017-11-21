function HCSimulator(baseV, baseR, gammaVal, betaVal, maxT, figNum)
    figure(figNum);
    title(['\fontsize{28}Homicidal Chauffeur, \gamma = ' num2str(gammaVal) ', \beta = ' ...
        num2str(betaVal)]);
    hold on;

    rInit = betaVal*baseR;
    count = 1;
    while (rInit < maxT*baseV)
        thetaInit = 0;
        while (thetaInit <= pi)
            [xCap, yCap] = HCForwardTime(baseV, ...
                baseR, gammaVal, betaVal, maxT, rInit, thetaInit);

            if (size(xCap) > 0)
                traj = plot(xCap, yCap, 'col', [0.5 0.5 0.5]);
            else
                disp(['thetaInit' num2str(thetaInit)]);
            end

            count = count + 1;
            thetaInit = thetaInit + 0.01;
        end

        rInit = rInit + 1;
    end

    [caughtData, escapedData] = loadDataResults(gammaVal, betaVal, figNum);
    
    if (size(caughtData) > 0)
        caught = plot(caughtData(:,1), caughtData(:,2), 'r*');
    else
        caught = plot(nan, nan, 'r*');
    end
    
    if (size(escapedData) > 0)
        escaped = plot(escapedData(:,1), escapedData(:,2), 'b*');
    else
        escaped = plot(nan, nan, 'b*');
    end

    axis equal;
    xlabel('x (m)', 'fontsize', 20);
    ylabel('y (m)', 'fontsize', 20);

    legend([traj, escaped, caught], 'Trajectories resulting in capture', ...
        'AirSim did not result in capture', 'AirSim resulted in capture', ...
        'location', 'northwest');
end