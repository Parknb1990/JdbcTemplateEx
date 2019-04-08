package spring.ex.bbs.Command;

import java.util.ArrayList;

import org.springframework.ui.Model;

import spring.ex.bbs.DAO.BDao;
import spring.ex.bbs.DTO.BDto;

public class BListCommand implements BCommand {

	@Override
	public void execute(Model model) {
		// TODO Auto-generated method stub
		
		BDao dao = new BDao();
		ArrayList<BDto> dtos = dao.list();
		model.addAttribute("list", dtos);
	}

}
