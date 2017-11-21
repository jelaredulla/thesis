function p = HCRetroStraight(baseV, baseR, gammaVal, betaVal, x1, y1, maxT, ...
    phi, figNum)
    v_E = gammaVal * baseV;
    v_P = baseV;
    minR = baseR;
    captureL = betaVal * baseR;
    
    
    
    x(1) = captureL;
    y(1) = 0;
    
    x(2) = x1;
    y(2) = y1;
        
    dTau = 0.01;
    
    tau = 0;
    count = 2;
    while (tau <= maxT)
        psi = atan2(y(count), x(count));
        %psi = atan(1/tan((v_P/minR)*phi*dTau));
%         psi = (v_P/minR)*phi*dTau;
        x(count + 1) = x(count) + (v_P - (v_P/minR)*phi*y(count) - ...
            v_E*cos(psi))*dTau;
        
        y(count + 1) = y(count) + ...
            ((v_P/minR)*phi*x(count) - v_E*sin(psi))*dTau;
                
        count = count + 1;
        
        if ((sign(y(count)) ~= sign(phi)) || ...
                ((x(count)^2 + y(count)^2) < captureL^2))
            break
        end
        
        tau = tau + dTau;
    end

%     if (phi == 1)
%         colour = [91 207 244] / 255;
%     else
%         colour = [207 244 91] / 255;
%     end
    
    colour = [91 207 244] / 255;    
    
    figure(figNum);
    hold on;
    p = plot(x, y, 'col', colour);
    p.Color(4) = 0.3;
end
