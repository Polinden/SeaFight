package wolper.controller;

import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import wolper.logic.*;


	@Controller
	public class HomeController {


		@Autowired
		DataSource dataSource;
		@Autowired
		GamerDAO gamerDAO;

		@Autowired
		CrossGamerInfoBuss crossGamerInfoBuss;

		@Autowired
		SessionServise sesserv;

		SimpMessageSendingOperations messaging;

		@Autowired
		public void setMessaging(SimpMessageSendingOperations messaging) {
			this.messaging = messaging;
		}


		@RequestMapping(value = {"/", "/home"})
		public ModelAndView test1(HttpServletResponse response) throws IOException {
			return new ModelAndView("home");
		}

		@RequestMapping(value = "/papa")
		public ModelAndView test2(HttpServletResponse response, Model model) throws IOException {
			model.addAttribute("name", "Денис");
			return new ModelAndView("papas");
		}

		@RequestMapping(value = "/login")
		public ModelAndView login(HttpServletResponse response, Model model) throws IOException {
			return new ModelAndView("login");
		}

		@RequestMapping(value = "/error")
		public ModelAndView error(HttpServletResponse response, Model model) throws IOException {
			return new ModelAndView("error");
		}

		@RequestMapping(value = "/reg_error")
		public ModelAndView reg_error(HttpServletResponse response, Model model) throws IOException {
			return new ModelAndView("reg_error");
		}

		//Контроллер регистрации - попытка залогиниться дважды при открытой сессии в другом окне
		@RequestMapping(value = "/double_reg/{name}")
		public ModelAndView double_reg_confirm(@PathVariable("name") String name) throws IOException {
			ModelAndView modelAndView = new ModelAndView("double_reg");
			modelAndView.addObject("name", name);
			return modelAndView;
		}

		//Контроллер регистрации - логринимся занова а прошлы сессии надежно убиваем
		@RequestMapping(value = "/double_reg_final/{name}")
			public ModelAndView double_reg_decided(@PathVariable("name") String name, ServletRequest request) throws IOException {
			sesserv.expireAndKillUserSessions(name, request.getLocalPort());
			//TODO передалать на POST - так будет безопаснее.
			// Злой польщователь не сможет указав логин в URL завалить другого пользователя
			return new ModelAndView("redirect:/home");
		}

		//Контроллер регистрации нового игрока
		@RequestMapping(value = "/register")
		public ModelAndView register(HttpServletResponse response, Model model) throws IOException {
			Gamer gamer = new Gamer();
			model.addAttribute("gamer", gamer);
			return new ModelAndView("register");
		}

		//Контроллер входа нового игрока
		@RequestMapping(value = "/goin")
		public ModelAndView comein(HttpServletResponse response) throws IOException {
			return new ModelAndView("redirect:mainflow");
		}


		//Контроллер входа нового игрока
		@RequestMapping(value = "/successreg")
		public ModelAndView successreg(HttpServletResponse response) throws IOException {
			return new ModelAndView("successreg");
		}

		//Контроллер регистрации нового игрока
		@RequestMapping(value = "/register", method = RequestMethod.POST)
		public ModelAndView getregistered(@ModelAttribute("gamer") @Valid Gamer gamer,
										  BindingResult result,
										  @RequestParam("password2") String password2)  {

			//Валидация формы
			if (result.hasErrors()) return new ModelAndView("register");
			if (!gamer.getPassword().equals(password2)) return new ModelAndView("missedpasword");

			//Проверка совпадения имени игрока
			if (gamerDAO.ifDoubleGamer(gamer.getName())) return new ModelAndView("reg_error");

			//Сохранить игрока в базе данных
			gamerDAO.saveGamer(gamer);

			//По каким то причинам вход пользователя через АПИ (ниже) не позволяет контролировать таймаут сессии
			//пока не разобрался почему так!!!! Приходиться возвращать пользователя на страницу логина
			//Authentication request = new UsernamePasswordAuthenticationToken(gamer.getName(), gamer.getPassword());
			//SecurityContextHolder.getContext().setAuthentication(request);
			return new ModelAndView("redirect:successreg");
		}


		//Свомп контроллеры для передачи игровой инфорамации
		@MessageMapping("/infoExchange")
		public void handleSubscription(String name) {
			String [] names = name.split("&");
			//Полчено приглашение
			if (names[0].equals("invite")) {
				crossGamerInfoBuss.inviteOneAnother(names[1], names[2]);
			}
			//Todo!!! Сделать полноценный обмен сообщениями между игроками
		}

	}




	//Обработка исключений с выводом сообщений в виде веб-страниц
	@ControllerAdvice
	class GlobalControllerExceptionHandler {
	@ExceptionHandler(LogicEception.class)
		public ModelAndView forbiden(LogicEception ex) {
			ModelAndView modelAndView = new ModelAndView("exception");
			modelAndView.addObject("errCode", ex.getErrCode());
			modelAndView.addObject("errMsg", ex.getErrMsg());
			return modelAndView;
		}
	}
