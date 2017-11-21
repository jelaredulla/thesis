PATH = "C:/AirSimOct/thesis/JavaClient/AirSimJavaPlayer/"
def formatDataResults(gamma, beta):
    folderName = PATH + "type-1_g-{0:.3f}_b-{1:.3f}/".format(gamma, beta)
    filename = folderName + "data.txt" 

    print(filename)
    
    fd = open(filename, 'r')

    caughtFile = open(folderName + "caught.txt", "w")
    escapedFile = open(folderName + "escaped.txt", "w")

    for line in fd:
        line = line.strip()

        chunks = line.split('=')

        firstSection = chunks[0].split()

        result = firstSection[5]

        pPos = chunks[1].split("[")[1].split("]")[0]
        ePos = chunks[2].split("[")[1].split("]")[0]

        xP, yP, _ = pPos.split(',')
        xE, yE, _ = ePos.split(',')

        x = float(xE) - float(xP)
        y = float(yE) - float(yP)

        coords = "{} {}\n".format(x, y)
        if (result == "true"):
            caughtFile.write(coords)
        elif (result == "false"):
            escapedFile.write(coords)

    fd.close()
    caughtFile.close()
    escapedFile.close()
                

gammas = [0.1, 0.2, 0.5, 0.8, 0.95, 1, 0.8, 0.8, 0.8, 0.8, 0.8]
betas = [0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.2, 0.4, 0.6, 0.8, 1]

for i in range(len(gammas)):
    try:
        formatDataResults(gammas[i], betas[i])
    except:
        print(gammas[i], betas[i])
