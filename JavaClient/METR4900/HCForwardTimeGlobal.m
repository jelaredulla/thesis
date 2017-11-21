function [t, caught] = HCForwardTimeGlobal(baseV, baseR, gammaVal, betaVal, maxT, ...
    rInit, thetaInit, figNum)

    caught = 0;
    
    v_E = gammaVal * baseV;
    v_P = baseV;
    minR = baseR;
    captureL = betaVal * baseR;
    
    x_e(1) = rInit*cos(thetaInit);
    y_e(1) = rInit*sin(thetaInit);
    
    x_p(1) = 0;
    y_p(1) = 0;
    theta_p = 0;   
    
    
    dt = 0.01;
    t = 0;
    count = 1;
    
       
%     figure(figNum);
%     hold on;
    while (t <= maxT)
        
        xRel = (x_e(count) - x_p(count))*cos(theta_p) + ...
            (y_e(count) - y_p(count))*sin(theta_p);
        yRel = -(x_e(count) - x_p(count))*sin(theta_p) + ...
            (y_e(count) - y_p(count))*cos(theta_p);
        
        if (xRel^2 + yRel^2 < captureL^2)
            caught = 1;
            break;
        end
        
        if (yRel == 0)
            if (xRel < 0)
                phi = 1;
            else
                phi = 0;
            end
        else
            phi = sign(yRel);
        end

        theta_p = theta_p + (v_P/minR)*phi*dt;
        
        psi = theta_p + atan2(yRel, xRel);
        
        theta_e = psi;
        
        x_e(count + 1) = x_e(count) + v_E*cos(theta_e)*dt;
        y_e(count + 1) = y_e(count) + v_E*sin(theta_e)*dt;
        
        x_p(count + 1) = x_p(count) + v_P*cos(theta_p)*dt;
        y_p(count + 1) = y_p(count) + v_P*sin(theta_p)*dt;
        
%         plot([x_e(count) x_e(count+1)], [y_e(count) y_e(count+1)], 'b-');
%         plot([x_p(count) x_p(count+1)], [y_p(count) y_p(count+1)], 'r-');
        
        count = count + 1;
                
        t = t + dt;
%         pause(dt);
    end
    
    if (figNum)
        figure(figNum);
        hold on;

        plot(x_e, y_e, 'b-');
        plot(x_p, y_p, 'r-');

        filename = sprintf('rInit-%.3f_thetaInit-%.3f.png', rInit, thetaInit);
        print(filename, '-dpng');

        close(figNum);
    end
end
