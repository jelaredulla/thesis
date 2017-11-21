function HCRegionMaker(baseV, baseR, gammaVal, betaVal, maxT, figNum, ...
    AirSimDataWanted)
    
    if (AirSimDataWanted)
        simName = 'AirSim: ';
    else
        simName = 'MATLAB sim: ';
    end
    
    figure(figNum);
    title(['\fontsize{24}' simName 'Homicidal Chauffeur, \gamma = ' ...
        num2str(gammaVal) ', \beta = ' num2str(betaVal)]);
    hold on;
    set(gcf, 'pos', [500, 50, 1200, 800]);

    [turn, straight] = HCRetro(baseV, baseR, gammaVal, betaVal, maxT, figNum);

%     [caughtData, escapedData] = loadDataResults(gammaVal, betaVal);
%     [caughtDataMATLAB, escapedDataMATLAB] = runHCGame(baseV, baseR, ...
%         gammaVal, betaVal, maxT, figNum, 0);
%         
%     caught = plot(nan, nan, 'r*');
%     escaped = plot(nan, nan, 'g*');
%     
%     if (AirSimDataWanted)
%         if (size(caughtData) > 0)
%             plot(caughtData(:,1), caughtData(:,2), 'r*');
%         end
% 
%         if (size(escapedData) > 0)
%             plot(escapedData(:,1), escapedData(:,2), 'go');
%         end
%     else    
%         if (size(caughtDataMATLAB) > 0)
%             plot(caughtDataMATLAB(:,1), caughtDataMATLAB(:,2), 'r*');
%         end
% 
%         if (size(escapedDataMATLAB) > 0)
%             plot(escapedDataMATLAB(:,1), escapedDataMATLAB(:,2), 'go');
%         end
%     end
%     
%     allData = [caughtData; escapedData; ...
%         caughtDataMATLAB; escapedDataMATLAB];
% 
%     axis([min(allData(:, 1)) max(allData(:, 1)) -4 max(allData(:, 2))]);
%     xlabel('x (m)', 'fontsize', 20);
%     ylabel('y (m)', 'fontsize', 20);
% 
% 
%     lgd = legend([turn, straight, escaped, caught], ...
%         'Trajectories resulting in capture, always turning', ...
%         'Trajectories resulting in capture, ending straight', ...
%         'Simulation did not result in capture', ...
%         'Simulation resulted in capture', 'location', 'southoutside');
%     lgd.FontSize = 14;
%     
%     filename = sprintf('%d_hc_%.3f_%.3f', AirSimDataWanted, ...
%         gammaVal, betaVal);
%     
%     cd('allPlotFigs');
%     savefig([filename '.fig']);
%     cd ..
%     
%     cd('allPlots');
%     print([filename '.png'], '-dpng');
%     cd ..
end