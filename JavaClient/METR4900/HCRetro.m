function [turn, straight] = HCRetro(baseV, baseR, gammaVal, betaVal, maxT, figNum)
    v_E = gammaVal * baseV;
    v_P = baseV;
    minR = baseR;
    captureL = betaVal * baseR;
    
    dTau = 0.25;
    tau = 0;
    while (tau <= maxT)
        x = captureL + (v_P - v_E)*tau;
        y = 0;
        
        HCRetroStraight(baseV, baseR, gammaVal, betaVal, x, y, ...
            maxT - tau, 1, figNum);
    
        straight = HCRetroStraight(baseV, baseR, gammaVal, betaVal, x, y, ...
            maxT - tau, -1, figNum);
        
        tau = tau + dTau;
    end 
    
    dS1 = 0.05;
    s1 = 0;
    while (s1 < (2*pi))
        x = captureL*cos(s1);
        y = captureL*sin(s1);
        
        HCRetroPhiFixed(baseV, baseR, gammaVal, betaVal, s1, x, y, ...
            maxT, 1, figNum);
    
        turn = HCRetroPhiFixed(baseV, baseR, gammaVal, betaVal, s1, x, y, ...
            maxT, -1, figNum);
    
        s1 = s1 + dS1;
    end
end
