baseV = 3;
baseR = 8;
maxT = 60;

gammas = [0.1, 0.2, 0.5, 0.8, 0.95, 1.0, 0.8, 0.8, 0.8, 0.8, 0.8];
betas = [0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.2, 0.4, 0.6, 0.8, 1.0];

for i = 1:length(gammas)
    HCRegionMaker(baseV, baseR, gammas(i), betas(i), maxT, 2*i-1, 0);
%     HCRegionMaker(baseV, baseR, gammas(i), betas(i), maxT, 2*i, 1);
end

