function p = HCRetroPhiFixed(baseV, baseR, gammaVal, betaVal, s1, x1, y1, maxT, ...
    phi, figNum)
    v_E = gammaVal * baseV;
    v_P = baseV;
    minR = baseR;
    captureL = betaVal * baseR;
    
    x(1) = x1;
    y(1) = y1;
    
    dTau = 0.01;
    
    tau = 0;
    count = 1;
    while (tau <= maxT)
        %psi = atan(1/tan(s1 + (v_P/minR)*phi*dTau));
        psi = atan2(y(count), x(count));
%         psi = s1 + (v_P/minR)*phi*dTau;
        x(count + 1) = x(count) + (v_P - (v_P/minR)*phi*y(count) - ...
            v_E*cos(psi))*dTau;
        
        y(count + 1) = y(count) + ...
            ((v_P/minR)*phi*x(count) - v_E*sin(psi))*dTau;
                
        count = count + 1;
        
        if ((sign(y(count)) ~= sign(phi)) || ...
                (abs(y(count)) < 0.01) || ...
                ((x(count)^2 + y(count)^2) < captureL^2))
            break
        end
        
        tau = tau + dTau;
    end

    figure(figNum);
    hold on;
    
%     if (phi == 1)
%         colour = [0 0.5 0.5];
%     else
%         colour = [0.5 0.5 0];
%     end
    colour = [0, 0, 1];
    
    p = plot(x, y, 'col', colour);
    p.Color(4) = 0.3;
end
