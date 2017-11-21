function [xCap, yCap] = HCForwardTime(baseV, baseR, gammaVal, betaVal, maxT, ...
    rInit, thetaInit)
% , figNum)
    xCap = [];
    yCap = [];

    v_E = gammaVal * baseV;
    v_P = baseV;
    minR = baseR;
    captureL = betaVal * baseR;
    
    x(1) = rInit*cos(thetaInit);
    y(1) = rInit*sin(thetaInit);
    
    dt = 0.01;
    t = 0;
    count = 1;
    
    
        
    
    figure(4);
    hold on;
    grid on;
%     axis([-5 5 -5 5]);
    while (t <= maxT)
                
        if (y(count) == 0)
            if (x(count) < 0)
                phi = 1;
            else
                phi = 0;
            end
        else
            phi = sign(y(count));
        end
        
        psi = atan2(y(count), x(count));
                
        x(count + 1) = x(count) + (-v_P + (v_P/minR)*phi*y(count) + ...
            v_E*cos(psi))*dt;
        
        y(count + 1) = y(count) + ...
            (-(v_P/minR)*phi*x(count) + v_E*sin(psi))*dt;
        
        plot([x(count) x(count+1)], [y(count) y(count+1)], 'b-');
                
        count = count + 1;
        
        if (x(count)^2 + y(count)^2 < captureL^2)
            break
        end
        
        t = t + dt;
        pause(dt);
    end
    
    epsilon = 0.05;

    if ((x(count)^2 + y(count)^2) < (captureL + epsilon)^2)
        disp('caught :)');
        xCap = x;
        yCap = y;
%         figure(figNum);
%         hold on;
%         
%         plot(x, y, 'col', [0.5 0.5 0.5]);
    end
    
end
