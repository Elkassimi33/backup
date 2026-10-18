import 'package:shop_app_front/api/model/response/LigneCommandResponse.dart';

class CommandResponse {

    int userId;
    DateTime commandDate;

    List<Lignecommandresponse> ligneCommand;

    CommandResponse({required this.userId, required this.ligneCommand, required this.commandDate});

    bool operator ==(Object other){
        if (identical(other, this)) return true;
        return other is CommandResponse && this.userId == other.userId && this.commandDate == other.commandDate;
    }

    int get hashCode => Object.hash(userId, DateTime);

}
