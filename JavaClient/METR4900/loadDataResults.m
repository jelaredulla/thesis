function [caughtData, escapedData] = loadDataResults(gammaVal, betaVal)
    PATH = 'C:/AirSimOct/thesis/JavaClient/AirSimJavaPlayer';
    folderName = sprintf('%s/type-1_g-%.3f_b-%.3f/', PATH, gammaVal, ...
        betaVal);
    caughtData = load(strcat(folderName, 'caught.txt'));
    escapedData = load(strcat(folderName, 'escaped.txt')); 
end