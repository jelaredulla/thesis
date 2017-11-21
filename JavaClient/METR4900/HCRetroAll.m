function p = HCRetroAll(baseV, baseR, gammaVal, betaVal, s1, maxT, ...
    figNum)
    v_E = gammaVal * baseV;
    v_P = baseV;
    minR = baseR;
    captureL = betaVal * baseR;
    
    x(1) = captureL*cos(s1)
    y(1) = captureL*sin(s1)
    
    
    phi = 0;
    
    
    dTau = 0.01;
    
    tau = 0;
    count = 1;
    while (tau <= maxT)
        
        Vx = captureL*cos(s1 + (v_P/minR)*phi*dTau);
        Vy = captureL*sin(s1 + (v_P/minR)*phi*dTau);
        phi = sign(Vy*x(count) - Vx*y(count));  
        
        psi = atan(1/tan(s1 + (v_P/minR)*phi*dTau));
        
        
                        
        x(count + 1) = x(count) + (v_P - (v_P/minR)*phi*y(count) - ...
            v_E*cos(psi))*dTau;
        
        y(count + 1) = y(count) + ...
            ((v_P/minR)*phi*x(count) - v_E*sin(psi))*dTau;
                
        count = count + 1;
        
        if (x(count)^2 + y(count)^2 < captureL^2)
            break
        end
        
        
        
        tau = tau + dTau;
        
      
    end

    figure(figNum);
    hold on;
    p = plot(x, y, 'col', [0 0 1]);
    p.Color(4) = 0.3;
end
