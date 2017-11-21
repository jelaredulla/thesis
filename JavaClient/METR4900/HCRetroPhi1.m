function HCRetroPhi1(baseV, baseR, gammaVal, betaVal, s1, maxT)
    v_E = gammaVal * baseV;
    v_P = baseV;
    minR = baseR;
    captureL = betaVal * baseR;
    
    x(1) = captureL*cos(s1);
    y(1) = captureL*sin(s1);
    
    dTau = 0.01;
    tau = 0;
    count = 1;
    
        
    
%     figure();
%     hold on;
%     grid on;
%     axis([-5 5 -5 5]);

    while (tau < maxT)
        
        phi = 1;
        syms xt yt;
        
        A = x(count);
        B = y(count);
        eq1 = A - ((v_P/minR)*phi*yt + v_E*cos(atan2(yt, xt)) - v_P)*dTau - xt;
        eq2 = B - (v_E*sin(atan2(yt, xt)) - (v_P/minR)*phi*xt)*dTau - yt;
        
        soln = solve([eq1; eq2], xt, yt);
        
        if (~consistentPhi(soln.xt, soln.yt, phi))
            phi = -1;
            syms xt yt;
        
            A = x(count);
            B = y(count);
            eq1 = A - ((v_P/minR)*phi*yt + v_E*cos(atan2(yt, xt)) - v_P)*dTau - xt;
            eq2 = B - (v_E*sin(atan2(yt, xt)) - (v_P/minR)*phi*xt)*dTau - yt;

            soln = solve([eq1; eq2], xt, yt);
        end
        
        if (~consistentPhi(soln.xt, soln.yt, phi))
            phi = 0;
            syms xt yt;
        
            A = x(count);
            B = y(count);
            eq1 = A - ((v_P/minR)*phi*yt + v_E*cos(atan2(yt, xt)) - v_P)*dTau - xt;
            eq2 = B - (v_E*sin(atan2(yt, xt)) - (v_P/minR)*phi*xt)*dTau - yt;

            soln = solve([eq1; eq2], xt, yt);
        end
                                
        x(count + 1) = soln.xt;
        
        y(count + 1) = soln.yt;
        
%         plot([x(count) x(count+1)], [y(count) y(count+1)], 'b-');
                
        count = count + 1;
        
        if (x(count)^2 + y(count)^2 < captureL^2)
            break
        end
        
        tau = tau + dTau;
%         pause(0.01);
    end

    figure(4);
    hold on;
    grid on;
    plot(x, y, 'b-');
    axis equal;
end
